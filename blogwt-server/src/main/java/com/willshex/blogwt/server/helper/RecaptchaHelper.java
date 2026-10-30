//
//  RecaptchaHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.willshex.gson.web.service.client.HttpException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RecaptchaHelper {

	private static final Logger LOG = Logger
			.getLogger(RecaptchaHelper.class.getName());

	private static URLFetchService client = URLFetchServiceFactory
			.getURLFetchService();

	public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	private static final String ENTERPRISE_URL = "https://recaptchaenterprise.googleapis.com/v1/projects/";
	private static final String CLOUD_PLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
	public static final String ENTERPRISE_ACTION = "submit";
	private static final double MIN_SCORE = 0.5;

	public static boolean isHuman(String input, String key) {
		boolean isHuman = false;

		try {
			String requestData = "secret=" + key + "&response=" + input + "&";
			URL url = new URL(VERIFY_URL);

			HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);

			HTTPHeader contentTypeHeader = new HTTPHeader("Content-Type",
					"application/x-www-form-urlencoded");
			request.setHeader(contentTypeHeader);
			request.setPayload(requestData.getBytes(ServletHelper.UTF8));

			String responseText = null;
			HTTPResponse response = client.fetch(request);
			if (response.getResponseCode() >= 200
					&& response.getResponseCode() < 300
					&& (responseText = getResponseBody(response)) != null
					&& !"".equals(responseText)
					&& !"null".equalsIgnoreCase(responseText)) {
				JsonElement element = JsonParser.parseString(responseText);
				JsonObject jsonObject;
				if (element.isJsonObject()) {
					jsonObject = element.getAsJsonObject();
					element = jsonObject.get("success");

					if (element.isJsonPrimitive()) {
						isHuman = element.getAsBoolean();
					}
				}
			} else if (response.getResponseCode() >= 400)
				throw new HttpException(response.getResponseCode());
		} catch (Exception e) {
			if (LOG.isLoggable(Level.WARNING)) {
				LOG.log(Level.WARNING, "Error verifying error", e);
			}
		}

		return isHuman;
	}

	/**
	 * Verifies a token. With a site key the token comes from reCAPTCHA
	 * Enterprise, which has no secret and is checked with the assessments api
	 * as this app, otherwise it comes from the original checkbox and is checked
	 * with the secret key.
	 */
	public static boolean isHuman (String input, String key, String siteKey) {
		if (siteKey == null || siteKey.isEmpty()) {
			return isHuman(input, key);
		}

		boolean isHuman = false;

		try {
			String project = SystemProperty.applicationId.get();
			if (project.indexOf('~') >= 0) {
				project = project.substring(project.indexOf('~') + 1);
			}

			String accessToken = AppIdentityServiceFactory
					.getAppIdentityService()
					.getAccessToken(Arrays.asList(CLOUD_PLATFORM_SCOPE))
					.getAccessToken();

			JsonObject event = new JsonObject();
			event.addProperty("token", input);
			event.addProperty("siteKey", siteKey);
			event.addProperty("expectedAction", ENTERPRISE_ACTION);

			JsonObject assessment = new JsonObject();
			assessment.add("event", event);

			HTTPRequest request = new HTTPRequest(
					new URL(ENTERPRISE_URL + project + "/assessments"),
					HTTPMethod.POST);
			request.setHeader(new HTTPHeader("Content-Type",
					"application/json; charset=utf-8"));
			request.setHeader(
					new HTTPHeader("Authorization", "Bearer " + accessToken));
			request.setPayload(
					assessment.toString().getBytes(ServletHelper.UTF8));

			HTTPResponse response = client.fetch(request);
			String responseText = getResponseBody(response);

			if (response.getResponseCode() >= 200
					&& response.getResponseCode() < 300) {
				JsonObject result = JsonParser.parseString(responseText)
						.getAsJsonObject();
				JsonObject properties = result.getAsJsonObject("tokenProperties");
				JsonObject risk = result.getAsJsonObject("riskAnalysis");

				isHuman = properties != null && risk != null
						&& properties.get("valid").getAsBoolean()
						&& ENTERPRISE_ACTION
								.equals(properties.get("action").getAsString())
						&& risk.get("score").getAsDouble() >= MIN_SCORE;
			} else if (LOG.isLoggable(Level.WARNING)) {
				LOG.warning("reCAPTCHA Enterprise assessment failed ["
						+ response.getResponseCode() + "]: " + responseText);
			}
		} catch (Exception e) {
			if (LOG.isLoggable(Level.WARNING)) {
				LOG.log(Level.WARNING, "Error assessing reCAPTCHA token", e);
			}
		}

		return isHuman;
	}

	private static String getResponseBody(HTTPResponse response)
			throws UnsupportedEncodingException {
		return new String(response.getContent(), ServletHelper.UTF8);
	}
}

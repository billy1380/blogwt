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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
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

	private static String getResponseBody(HTTPResponse response)
			throws UnsupportedEncodingException {
		return new String(response.getContent(), ServletHelper.UTF8);
	}
}

//
//  BrevoHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Oct 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * Sends email through the Brevo transactional email api.
 *
 * @author William Shakour (billy1380)
 *
 */
public class BrevoHelper {
	private static final Logger LOG = Logger
			.getLogger(BrevoHelper.class.getName());

	public static final String ENDPOINT = "https://api.brevo.com/v3/smtp/email";

	public static boolean isEnabled () {
		Property enabled = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.BREVO_ENABLED);

		return !PropertyHelper.isEmpty(enabled)
				&& Boolean.parseBoolean(enabled.value)
				&& !PropertyHelper.isEmpty(PropertyServiceProvider.provide()
						.getNamedProperty(PropertyHelper.BREVO_API_KEY));
	}

	public static JsonObject payload (String fromEmail, String fromName,
			String to, String name, String subject, String body,
			boolean isHtml, Map<String, byte[]> attachments) {
		JsonObject payload = new JsonObject();

		JsonObject sender = new JsonObject();
		sender.addProperty("email", fromEmail);
		if (fromName != null && fromName.length() > 0) {
			sender.addProperty("name", fromName);
		}
		payload.add("sender", sender);

		JsonObject recipient = new JsonObject();
		recipient.addProperty("email", to);
		if (name != null && name.length() > 0) {
			recipient.addProperty("name", name);
		}
		JsonArray recipients = new JsonArray();
		recipients.add(recipient);
		payload.add("to", recipients);

		payload.addProperty("subject", subject);
		payload.addProperty(isHtml ? "htmlContent" : "textContent", body);

		if (attachments != null && attachments.size() > 0) {
			JsonArray files = new JsonArray();
			for (Map.Entry<String, byte[]> attachment : attachments
					.entrySet()) {
				JsonObject file = new JsonObject();
				file.addProperty("name", attachment.getKey());
				file.addProperty("content", Base64.getEncoder()
						.encodeToString(attachment.getValue()));
				files.add(file);
			}
			payload.add("attachment", files);
		}

		return payload;
	}

	public static boolean send (String fromEmail, String fromName, String to,
			String name, String subject, String body, boolean isHtml,
			Map<String, byte[]> attachments) {
		boolean sent = false;

		String key = PropertyHelper.value(PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.BREVO_API_KEY));

		try {
			HTTPRequest request = new HTTPRequest(new URL(ENDPOINT),
					HTTPMethod.POST);
			request.getFetchOptions().setDeadline(Double.valueOf(60.0));
			request.addHeader(new HTTPHeader("api-key", key.trim()));
			request.addHeader(new HTTPHeader("accept", "application/json"));
			request.addHeader(
					new HTTPHeader("content-type", "application/json"));
			request.setPayload(payload(fromEmail, fromName, to, name, subject,
					body, isHtml, attachments).toString()
							.getBytes(StandardCharsets.UTF_8));

			HTTPResponse response = URLFetchServiceFactory.getURLFetchService()
					.fetch(request);

			int code = response.getResponseCode();
			sent = code >= 200 && code < 300;

			if (sent) {
				if (LOG.isLoggable(Level.INFO)) {
					LOG.info("Email sent through Brevo.");
				}
			} else {
				LOG.warning("Brevo refused the email with code [" + code
						+ "] and body ["
						+ new String(response.getContent(),
								StandardCharsets.UTF_8)
						+ "]");
			}
		} catch (MalformedURLException e) {
			LOG.log(Level.SEVERE, "Brevo endpoint is not a valid url", e);
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Error sending email through Brevo", e);
		}

		return sent;
	}
}

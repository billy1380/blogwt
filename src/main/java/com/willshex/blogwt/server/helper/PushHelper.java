//
//  PushHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
import com.willshex.blogwt.server.helper.push.AndroidMessage;
import com.willshex.blogwt.server.helper.push.IosMessage;
import com.willshex.blogwt.server.helper.push.Message;
import com.willshex.blogwt.server.helper.push.Payload;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.utility.JsonUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PushHelper {

	private static final Logger LOG = Logger
			.getLogger(PushHelper.class.getName());

	private static final String PUSH_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
	private static final Charset UTF8 = Charset.forName("UTF-8");

	public static void push (PushToken pushToken, String subject,
			String content) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine(
					"Pushing notification to [" + pushToken + "] with subject ["
							+ subject + "] message [" + content + "]");
		}

		String apiKey = PropertyHelper.value(PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.FIREBASE_API_KEY));

		if (apiKey != null) {
			Message message = null;
			switch (pushToken.platform) {
			case "android":
				message = new AndroidMessage().title(subject).body(content);
				break;
			case "ios":
				message = new IosMessage().title(subject).body(content);
				break;
			}

			Payload payload = new Payload().to(pushToken.value)
					.notification(message);

			try {
				URL endpoint = new URL(PUSH_ENDPOINT);
				HTTPRequest request = new HTTPRequest(endpoint,
						HTTPMethod.POST);

				HTTPHeader contentTypeHeader = new HTTPHeader("Content-Type",
						"application/json");
				request.setHeader(contentTypeHeader);

				HTTPHeader authHeader = new HTTPHeader("Authorization",
						"key=" + apiKey);
				request.setHeader(authHeader);

				String payloadString;
				request.setPayload((payloadString = JsonUtils
						.cleanJson(payload.toJson().toString()))
								.getBytes(UTF8));

				URLFetchService client = URLFetchServiceFactory
						.getURLFetchService();
				HTTPResponse response = client.fetch(request);

				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine("Sending push payload [" + payloadString + "]");
				}

				byte[] responseBytes;
				String responseText = null;

				if ((responseBytes = response.getContent()) != null) {
					responseText = new String(responseBytes, UTF8);
				}

				if (response.getResponseCode() >= 200
						&& response.getResponseCode() < 300
						&& responseText != null) {
					if ("".equals(responseText)
							|| "null".equalsIgnoreCase(responseText)) {
						if (LOG.isLoggable(Level.WARNING)) {
							LOG.warning("No push response text for payload");
						}
					} else {
						if (LOG.isLoggable(Level.FINE)) {
							LOG.fine("Push response [" + responseText + "]");
						}

						try {
							JsonElement el = (new JsonParser())
									.parse(responseText);

							if (el.isJsonObject()) {
								JsonObject ro = el.getAsJsonObject();

								if (ro.has("failure")) {
									JsonElement fel = ro.get("failure");

									if (fel.isJsonPrimitive()) {
										if (fel.getAsInt() == 1) {
											if (LOG.isLoggable(Level.WARNING)) {
												LOG.log(Level.WARNING,
														"Push failed: check response");
											}
										}
									}
								}
							}
						} catch (Exception e) {
							if (LOG.isLoggable(Level.WARNING)) {
								LOG.log(Level.WARNING,
										"Could not parse response ["
												+ responseText + "]",
										e);
							}
						}
					}
				} else if (response.getResponseCode() >= 400) {
					if (LOG.isLoggable(Level.SEVERE)) {
						LOG.severe("Response error ["
								+ response.getResponseCode() + "] and body ["
								+ responseText + "] for payload");
					}
				}
			} catch (IOException e) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.log(Level.WARNING, "Error sending push notification",
							e);
				}
			}
		} else {
			if (LOG.isLoggable(Level.INFO)) {
				LOG.info("Not sending push message because property ["
						+ PropertyHelper.FIREBASE_API_KEY
						+ "] has not been set");
			}
		}
	}

}

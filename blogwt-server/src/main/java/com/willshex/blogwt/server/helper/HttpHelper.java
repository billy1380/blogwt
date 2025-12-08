//
//  HttpHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 29 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * @author William Shakour (billy1380)
 *
 */
public class HttpHelper {

	private static final Logger LOG = Logger
			.getLogger(HttpHelper.class.getName());

	/**
	 * Fetches a remote url
	 * @param url
	 * @param payload
	 * @param method
	 * @return
	 */
	public static byte[] curl (URL url, byte[] payload, HTTPMethod method,
			Map<String, String> headers) {
		byte[] content = null;
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();

		try {
			HTTPRequest request = new HTTPRequest(url, method);
			request.getFetchOptions().setDeadline(Double.valueOf(60.0));

			if (headers != null) {
				headers.forEach(
						(k, v) -> request.addHeader(new HTTPHeader(k, v)));
			}

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.log(Level.FINEST, "Fetching response");
			}

			if (payload != null) {
				request.setPayload(payload);
			}

			HTTPResponse response = fetcher.fetch(request);

			int responseCode = response.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				content = response.getContent();

				if (content == null || content.length == 0) {
					LOG.log(Level.SEVERE, String.format(
							"Response for [%s] was empty", url.toString()));
				} else {
					if (LOG.isLoggable(Level.INFO)) {
						LOG.info(String.format(
								"Recievend [%d] bytes for request",
								content.length));
					}

					if (LOG.isLoggable(Level.FINEST)) {
						LOG.log(Level.FINEST, new String(content, "UTF-8"));
					}
				}
			} else {
				LOG.log(Level.SEVERE, String.format(
						"Http error occured for request to [%s] with code [%d]",
						url.toString(), responseCode));
			}
		} catch (IOException e) {
			if (LOG.isLoggable(Level.SEVERE)) {
				LOG.log(Level.SEVERE,
						String.format("Error fetching response for url [%s]",
								url.toString()),
						e);
			}
		}

		return content;
	}

	public static byte[] curl (String endpoint, byte[] payload,
			HTTPMethod method, Map<String, String> headers) {
		byte[] content = null;

		if (LOG.isLoggable(Level.FINE)) {
			LOG.log(Level.FINE, String.format(
					"calling endpoint [%s] with payload [%s] using method [%s]",
					endpoint, payload == null ? null : new String(payload),
					method.toString()));
		}

		try {
			URL url = new URL(endpoint);
			content = curl(url, payload, method, headers);
		} catch (MalformedURLException e) {
			if (LOG.isLoggable(Level.SEVERE)) {
				LOG.log(Level.SEVERE, String.format(
						"Error creating url from endpoint [%s]", endpoint), e);
			}
		}

		return content;
	}

	public static byte[] curl (String endpoint) {
		return curl(endpoint, null, null, null);
	}

	public static byte[] curl (String endpoint, Map<String, String> headers) {
		return curl(endpoint, null, HTTPMethod.POST, headers);
	}

	public static byte[] curl (String endpoint, HTTPMethod method,
			Map<String, String> headers) {
		return curl(endpoint, null, method, headers);
	}

	public static byte[] curl (String endpoint, HTTPMethod method) {
		return curl(endpoint, null, method, null);
	}

}

//
//  InlineHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Jul 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Stack;
import java.util.function.UnaryOperator;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * @author William Shakour (billy1380)
 *
 */
public class InlineHelper {
	private static final MemcacheService CACHE = MemcacheServiceFactory
			.getMemcacheService();
	private static final AsyncMemcacheService ASYNC_CACHE = MemcacheServiceFactory
			.getAsyncMemcacheService();

	public static String inline (String url, UnaryOperator<String> success) {
		return inline(url, success, null, true);
	}

	public static String inline (String url, UnaryOperator<String> success,
			UnaryOperator<String> fallback) {
		return inline(url, success, fallback, true);
	}

	public static String inline (String url, UnaryOperator<String> success,
			UnaryOperator<String> fallback, boolean useCache) {
		String inline = null;

		if (useCache) {
			inline = (String) CACHE.get(url);
		}

		if (inline == null) {
			inline = "";

			try {
				HTTPRequest request = new HTTPRequest(new URL(absolute(url)),
						HTTPMethod.GET);
				URLFetchService client = URLFetchServiceFactory
						.getURLFetchService();
				HTTPResponse response = client.fetch(request);

				byte[] responseBytes;
				String responseText = null;

				if ((responseBytes = response.getContent()) != null) {
					responseText = new String(responseBytes,
							ServletHelper.UTF8);
				}

				if (response.getResponseCode() >= 200
						&& response.getResponseCode() < 300
						&& responseText != null) {
					inline = success.apply(fixRelativeUrls(responseText, url));
					if (useCache) {
						ASYNC_CACHE.put(url, inline);
					}
				}
			} catch (IOException ex) {
				// could not get it on the server, let the browser sort it out
				if (fallback != null) {
					inline = fallback.apply(url);
				}
			}
		}

		return inline;
	}

	public static String js (String url) {
		return js(url, true);
	}

	public static String js (String url, boolean useCache) {
		return inline(url, c -> "<script>" + c + "</script>",
				u -> "<script async type=\"text/javascript\" src=\"" + u
						+ "\"></script>",
				useCache);
	}

	public static String css (String url) {
		return inline(url, c -> "<style>" + c + "</style>",
				u -> "<link rel=\"stylesheet\" href=\"" + u + "\">");
	}

	private static boolean isAbsolute (String url) {
		return url.startsWith("https://") || url.startsWith("http://")
				|| url.startsWith("/");
	}

	private static String absolute (String url) {
		return isAbsolute(url) ? url
				: (ServletHelper.constructBaseUrl() + "/" + url);
	}

	private static String fixRelativeUrls (String content, String url) {
		String fixed = content;

		// only replace urls if the path is absolute
		if (isAbsolute(url)) {
			Stack<String> p = pathStack(url);
			p.pop();
			fixed = content.replace("url(.",
					"url(" + String.join("/", p) + "/.");
		}

		return fixed;
	}

	private static Stack<String> pathStack (String path) {
		Stack<String> p = new Stack<>();
		p.addAll(Arrays.asList(path.split("/")));
		return p;
	}
}

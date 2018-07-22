//
//  EnvironmentHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Apr 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.utils.SystemProperty.Environment;
import com.google.appengine.api.utils.SystemProperty.Environment.Value;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EnvironmentHelper {

	private static final Logger LOG = Logger
			.getLogger(EnvironmentHelper.class.getName());

	private static final String DEV_NAMESPACE = "dev";
	private static final String APPSPOT_SUB_DOMAIN_SEPARATOR = "-dot-";

	public static boolean isDev () {
		return Environment.environment.value() == Value.Development;
	}

	public static void selectNamespace (boolean shared) {
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Selecting namespace for shared [" + shared + "]");
		}

		String baseUrl;
		if (!shared && (baseUrl = ServletHelper
				.constructBaseAddress(ContextAwareServlet.REQUEST.get()))
						.contains(APPSPOT_SUB_DOMAIN_SEPARATOR)) {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Domain name contains [" + APPSPOT_SUB_DOMAIN_SEPARATOR
						+ "]");
			}

			String[] split = baseUrl.split(APPSPOT_SUB_DOMAIN_SEPARATOR);
			String namespace;

			if (split.length > 0 && !"".equals(namespace = split[0])) {
				switch (namespace) {
				default:
					if (LOG.isLoggable(Level.FINE)) {
						LOG.fine(
								"Setting namespace to [" + DEV_NAMESPACE + "]");
					}

					NamespaceManager.set(DEV_NAMESPACE);
					break;
				}
			}
		} else {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Clearing namespace");
			}

			NamespaceManager.set("");
		}
	}
}

//
//  RecaptchaHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.logging.Logger;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RecaptchaHelper {

	private static final Logger LOG = Logger.getLogger(RecaptchaHelper.class
			.getName());

	public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	public static boolean isHuman (String input, String key) {
		return false;
	}
}

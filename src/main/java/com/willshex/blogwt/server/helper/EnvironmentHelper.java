//
//  EnvironmentHelper.java
//  bidly
//
//  Created by William Shakour (billy1380) on 30 Apr 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import com.google.appengine.api.utils.SystemProperty.Environment;
import com.google.appengine.api.utils.SystemProperty.Environment.Value;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EnvironmentHelper {
	public static boolean isDev () {
		return Environment.environment.value() == Value.Development;
	}
}

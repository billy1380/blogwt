//
//  ServletHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 5 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ServletHelper {

	/**
	 * @param request
	 * @return
	 */
	public static String constructBaseUrl (HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String url = scheme + "://" + serverName + ":" + serverPort;

		return url;
	}

}

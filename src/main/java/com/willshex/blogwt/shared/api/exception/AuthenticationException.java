//
//  AuthenticationException.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.shared.api.exception;

import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AuthenticationException extends ServiceException {

	private static final long serialVersionUID = -3370564777484950227L;

	/**
	 * @param code
	 * @param message
	 */
	public AuthenticationException(String username) {
		super(ApiError.AuthenticationFailed.getCode(), ApiError.AuthenticationFailed.getMessage(username));
	}

}

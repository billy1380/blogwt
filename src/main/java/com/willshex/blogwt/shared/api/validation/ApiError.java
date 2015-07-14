//
//  ApiError.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Apr 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.validation;

/**
 * @author William Shakour (billy1380)
 *
 */
public enum ApiError {

	JsonParseException(100000, "Invalid Json, could not be parsed"),
	InvalidValueNull(100001, "Invalid value null {0}"),
	TokenNoMatch(100002, "Invalid value does not match scheme for String: {0}"),
	DataTypeNoLookup(100003,
			"Invalid data type lookup, need id or other key for {0}"),
	DataTypeNotFound(100004, "Data type not found {0}"),
	AuthenticationFailed(100005, "Authentication failed for username {0}"),
	AuthorisationFailed(100006, "Authroisation failed for {0}"),
	ExistingSetup(100007, "Setup has already been completed for: {0}"),
	MissingProperties(100008, "One or more properties were missing in: {0}"),
	AuthenticationFailedBadPassword(100009,
			"Authentication failed, password does not match current login user {0}"), ;

	private static final String PARAM_0 = "\\{0\\}";
	private static final String PARAM_1 = "\\{1\\}";
	private static final String PARAM_2 = "\\{2\\}";

	private int code;
	private String message;

	ApiError (int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode () {
		return code;
	}

	public String getMessage () {
		return message;
	}

	public String getMessage (String parent) {
		return message.replaceAll(PARAM_0, parent == null ? "?" : parent);
	}

	public String getMessage (String parent, int minValue, int maxValue) {
		return getMessage(parent).replaceAll(PARAM_1,
				Integer.toString(minValue)).replaceAll(PARAM_2,
				Integer.toString(maxValue));
	}

	public boolean isCode (int value) {
		return code == value;
	}

}

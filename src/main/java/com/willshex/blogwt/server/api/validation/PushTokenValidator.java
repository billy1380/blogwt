//
//  PushTokenValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PushTokenValidator extends ApiValidator {
	private static final Class<PushToken> CLASS = PushToken.class;

	public static PushToken valiate (PushToken pushToken, String name)
			throws InputValidationException {
		notNull(pushToken, CLASS, name);

		pushToken.user = UserValidator.lookup(pushToken.user, name + ".user");

		notNull(pushToken.platform, String.class, name + ".platform");

		if (!(pushToken.platform.equalsIgnoreCase("ios")
				|| pushToken.platform.equalsIgnoreCase("android")))
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound,
					"Currently supported push systems either android or ios String:"
							+ name + ".platform");

		notNull(pushToken.value, String.class, name + ".value");
		validateLength(pushToken.value, 1, 1000, name + ".value");

		return pushToken;
	}
}

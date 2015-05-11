//
//  ApiValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Apr 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.lang.reflect.InvocationTargetException;

import com.willshex.gson.json.service.server.InputValidationException;
import com.willshex.gson.json.service.server.ServiceException;
import com.willshex.gson.json.service.shared.Request;
import com.willshex.blogwt.shared.api.validation.ApiError;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ApiValidator {

	public static String accessCode(String accessCode, String name) throws InputValidationException {
		if (accessCode == null) throwServiceError(InputValidationException.class, ApiError.InvalidValueNull, "String: " + name);

		if (!accessCode.matches("([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})"))
			throwServiceError(InputValidationException.class, ApiError.TokenNoMatch, name);

		// TODO: if all is good look it up

		return accessCode;
	}

	public static <R extends Request> R request(R input, Class<R> c) throws InputValidationException {
		if (input == null) throwServiceError(InputValidationException.class, ApiError.InvalidValueNull, c.getSimpleName() + ": input");

		return input;
	}

	public static <E extends ServiceException> void throwServiceError(Class<E> c, ApiError error, Object... params) throws E {
		E e = null;
		String message = "";

		if (params != null) {
			switch (params.length) {
			case 1:
				message = error.getMessage((String) params[0]);
				break;
			case 3:
				message = error.getMessage((String) params[0].toString(), (int) params[1], (int) params[2]);
			default:
				break;
			}
		}

		try {
			e = c.getDeclaredConstructor(int.class, String.class).newInstance(error.getCode(), message);
		} catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException ex) {
			throw new RuntimeException("Could not create ServiceException of type " + c.getCanonicalName(), ex);
		}

		throw e;
	}

}

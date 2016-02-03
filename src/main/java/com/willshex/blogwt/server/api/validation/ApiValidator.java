//
//  ApiValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Apr 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.lang.reflect.InvocationTargetException;

import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.gson.web.service.shared.Request;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ApiValidator {

	public static String accessCode (String accessCode, String name)
			throws InputValidationException {
		accessCode = validateToken(accessCode, name);

		// TODO: if all is good look it up

		return accessCode;
	}

	/**
	 * @param value
	 * @param min
	 * @param max
	 * @param field
	 * @throws InputValidationException 
	 */
	public static void validateLength (String value, int min, int max,
			String field) throws InputValidationException {
		if (value.length() < min || value.length() > max)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.BadLength, Integer.valueOf(min),
					Integer.valueOf(max), field);
	}

	public static String validateToken (String token, String name)
			throws InputValidationException {
		if (token == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, "String: " + name);

		if (!token
				.matches("([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})"))
			throwServiceError(InputValidationException.class,
					ApiError.TokenNoMatch, name);

		return token;
	}

	public static <R extends Request> R request (R input, Class<R> c)
			throws InputValidationException {
		if (input == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, c.getSimpleName() + ": input");

		return input;
	}

	public static <T> T notNull (T value, Class<?> c, String name)
			throws InputValidationException {
		if (value == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, c.getSimpleName() + ": " + name);

		return value;
	}

	public static <E extends ServiceException> void throwServiceError (
			Class<E> c, ApiError error, Object... params) throws E {
		E e = null;
		String message = "";

		if (params != null) {
			switch (params.length) {
			case 1:
				message = error.getMessage((String) params[0]);
				break;
			case 3:
				message = error.getMessage((String) params[2],
						((Integer) params[0]).intValue(),
						((Integer) params[1]).intValue());
			default:
				break;
			}
		}

		try {
			e = c.getDeclaredConstructor(int.class, String.class).newInstance(
					error.getCode(), message);
		} catch (IllegalAccessException | InstantiationException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex) {
			throw new RuntimeException(
					"Could not create ServiceException of type "
							+ c.getCanonicalName(), ex);
		}

		throw e;
	}

}

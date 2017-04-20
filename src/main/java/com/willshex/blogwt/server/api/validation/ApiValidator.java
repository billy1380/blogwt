//
//  ApiValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Apr 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.gson.web.service.shared.Request;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ApiValidator {

	public static interface Processor<T> {
		T process (T item, String name) throws InputValidationException;
	}

	public static final String WEB_ACCESS_CODE = "2bfe5f0e-9138-401c-8619-9a66f6367c9a";
	public static final String DEV_ACCESS_CODE = "d8d20842-a8f7-11e5-bf72-cf5f3bd13298";
	public static final String STATIC_ACCESS_CODE = "ded02740-5e12-11e5-b0c2-7054d251af02";

	private static final String[] ALLOWED_ACCESS_CODES = new String[] {
			WEB_ACCESS_CODE, DEV_ACCESS_CODE, STATIC_ACCESS_CODE };

	public static String accessCode (String accessCode, String name)
			throws InputValidationException {
		accessCode = validateToken(accessCode, name);

		boolean foundAllowedCode = false;

		for (String code : ALLOWED_ACCESS_CODES) {
			if (accessCode.equalsIgnoreCase(code)) {
				foundAllowedCode = true;
				break;
			}
		}

		if (!foundAllowedCode) throwServiceError(InputValidationException.class,
				ApiError.TokenNoMatch, name);

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
		if (token == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, "String: " + name);

		if (!token.matches(
				"([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})"))
			throwServiceError(InputValidationException.class,
					ApiError.TokenNoMatch, name);

		return token;
	}

	public static <R extends Request> R request (R input, Class<R> c)
			throws InputValidationException {
		if (input == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, c.getSimpleName() + ": input");

		return input;
	}

	public static <T> T notNull (T value, Class<?> c, String name)
			throws InputValidationException {
		if (value == null) throwServiceError(InputValidationException.class,
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
			e = c.getDeclaredConstructor(int.class, String.class)
					.newInstance(error.getCode(), message);
		} catch (IllegalAccessException | InstantiationException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex) {
			throw new RuntimeException(
					"Could not create ServiceException of type "
							+ c.getCanonicalName(),
					ex);
		}

		throw e;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Iterable<S>, S> T processAll (boolean nullable,
			T l, Processor<S> p, String type, String name)
			throws InputValidationException {
		if (l == null && !nullable)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		List<S> l1 = null;
		if (l != null) {
			int i = 0;
			l1 = new ArrayList<>();
			for (S s : l) {
				l1.add(p.process(s, name + "[" + i + "]"));
				i++;
			}

		}

		return (T) l1;
	}

}

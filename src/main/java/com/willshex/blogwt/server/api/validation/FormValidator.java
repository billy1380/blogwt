//
//  FormValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.HashMap;
import java.util.Map;

import com.willshex.blogwt.shared.api.datatype.Field;
import com.willshex.blogwt.shared.api.datatype.Form;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormValidator {
	private static final String type = Form.class.getSimpleName();

	public static Form validate (Form form, String name)
			throws InputValidationException {
		if (form == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		Map<String, Field> fields = new HashMap<String, Field>();
		for (Field field : form.fields) {
			// create lookup
			fields.put(field.name, field);
		}

		// verify captch

		return form;
	}
}

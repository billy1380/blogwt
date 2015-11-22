//
//  FormValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

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

		if (form.fields == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name + ".fields");

		if (form.fields.size() == 0)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.EmptyForm, type + ": " + name + ".fields");

		for (Field field : form.fields) {
			FieldValidator.validate(field, name + ".field[n]");
		}

		if (form.name != null
				&& (form.name.length() == 0 || form.name.length() > 512))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.BadLength, Integer.toString(1),
					Integer.toString(512), type + ": " + name + ".name");

		return form;
	}
}

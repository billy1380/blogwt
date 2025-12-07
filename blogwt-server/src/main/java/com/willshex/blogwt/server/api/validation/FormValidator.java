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
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormValidator extends ApiValidator {
	private static final String TYPE = Form.class.getSimpleName();

	public static Form validate (Form form, String name)
			throws InputValidationException {
		if (form == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		if (form.fields == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name + ".fields");

		if (form.fields.size() == 0)
			throwServiceError(InputValidationException.class,
					ApiError.EmptyForm, TYPE + ": " + name + ".fields");

		for (Field field : form.fields) {
			FieldValidator.validate(field, name + ".field[n]");
		}

		if (form.name != null) {
			validateLength(form.name, 1, 512, TYPE + ": " + name + ".name");
		}

		return form;
	}
}

//
//  FieldValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.helper.RecaptchaHelper;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Field;
import com.willshex.blogwt.shared.api.datatype.FieldTypeType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FieldValidator {
	private static final String type = Field.class.getSimpleName();

	public static Field validate (Field field, String name)
			throws InputValidationException {
		if (field == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		if (field.type == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name + ".type");

		if (field.type != FieldTypeType.FieldTypeTypeCaptcha
				&& field.name == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name + ".name");

		switch (field.type) {
		case FieldTypeTypeCaptcha:
			Property reCaptchaKey = PropertyServiceProvider.provide()
					.getNamedProperty(PropertyHelper.RECAPTCHA_API_KEY);

			if (!RecaptchaHelper.isHuman(field.value, reCaptchaKey.value))
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull, type + ": " + name);
			break;
		case FieldTypeTypeText:
			if (field.value == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull, type + ": " + name + "["
								+ field.name + "].value");

			ApiValidator.validateLength(field.value, 1, 512, type + ": " + name + "["
					+ field.name + "].value");
			break;
		case FieldTypeTypeLongText:
			if (field.value == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull, type + ": " + name + "["
								+ field.name + "].value");

			ApiValidator.validateLength(field.value, 1, 2048, type + ": "
					+ name + "[" + field.name + "].value");
			break;
		case FieldTypeTypeSingleOption:
			if (field.value == null)
				ApiValidator.throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull, type + ": " + name + "["
								+ field.name + "].value");

			ApiValidator.validateLength(field.value, 1, 512, type + ": " + name
					+ "[" + field.name + "].value");
			break;
		}

		return field;
	}

}

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
public class FieldValidator extends ApiValidator {
	private static final String TYPE = Field.class.getSimpleName();

	public static Field validate (Field field, String name)
			throws InputValidationException {
		if (field == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		if (field.type == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name + ".type");

		if (field.type != FieldTypeType.FieldTypeTypeCaptcha
				&& field.name == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name + ".name");

		switch (field.type) {
		case FieldTypeTypeCaptcha:
			Property reCaptchaKey = PropertyServiceProvider.provide()
					.getNamedProperty(PropertyHelper.RECAPTCHA_API_KEY);

			Property reCaptchaSiteKey = PropertyServiceProvider.provide()
					.getNamedProperty(PropertyHelper.RECAPTCHA_SITE_KEY);

			// a captcha from reCAPTCHA Enterprise has its site key as the
			// name, unless the site key is set in the properties
			if (!RecaptchaHelper.isHuman(field.value,
					reCaptchaKey == null ? null : reCaptchaKey.value,
					RecaptchaHelper.siteKey(
							reCaptchaSiteKey == null ? null
									: reCaptchaSiteKey.value,
							field.name)))
				throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull, TYPE + ": " + name);
			break;
		case FieldTypeTypeText:
			if (field.value == null)
				throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull,
						TYPE + ": " + name + "[" + field.name + "].value");

			validateLength(field.value, 1, 512,
					TYPE + ": " + name + "[" + field.name + "].value");
			break;
		case FieldTypeTypeLongText:
			if (field.value == null)
				throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull,
						TYPE + ": " + name + "[" + field.name + "].value");

			validateLength(field.value, 1, 2048,
					TYPE + ": " + name + "[" + field.name + "].value");
			break;
		case FieldTypeTypeSingleOption:
			if (field.value == null)
				throwServiceError(InputValidationException.class,
						ApiError.InvalidValueNull,
						TYPE + ": " + name + "[" + field.name + "].value");

			validateLength(field.value, 1, 512,
					TYPE + ": " + name + "[" + field.name + "].value");
			break;
		}

		return field;
	}

}

//  
//  SubmitFormActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.FormValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.EmailHelper;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Field;
import com.willshex.blogwt.shared.api.datatype.FieldTypeType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.ServiceException;

public final class SubmitFormActionHandler
		extends ActionHandler<SubmitFormRequest, SubmitFormResponse> {
	private static final Logger LOG = Logger
			.getLogger(SubmitFormActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (SubmitFormRequest input, SubmitFormResponse output)
			throws Exception {
		// send an email with the submitted fields
		ApiValidator.request(input, SubmitFormRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			output.session = input.session = SessionValidator
					.lookupCheckAndExtend(input.session, "input.session");
		}

		input.form = FormValidator.validate(input.form, "input.form");

		Property email = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.INCOMING_EMAIL);

		Property title = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.TITLE);

		StringBuffer body = new StringBuffer();
		for (Field field : input.form.fields) {
			if (field.type != FieldTypeType.FieldTypeTypeCaptcha) {
				body.append(field.name);
				body.append(":");
				body.append(field.value);
				body.append("\n\n");
			}
		}

		if (!EmailHelper.sendEmail(email.value, title.value,
				"[" + input.form.name + "] Form Submitted", body.toString(),
				false))
			ApiValidator.throwServiceError(ServiceException.class,
					ApiError.FailedToSendEmail, "input.form");

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected SubmitFormResponse newOutput () {
		return new SubmitFormResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}

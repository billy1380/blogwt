//  
//  UpdatePropertiesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.List;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.shared.StatusType;

public final class UpdatePropertiesActionHandler extends ActionHandler {
	private static final Logger LOG = Logger
			.getLogger(UpdatePropertiesActionHandler.class.getName());

	public UpdatePropertiesResponse handle (UpdatePropertiesRequest input) {
		LOG.finer("Entering updateProperties");
		UpdatePropertiesResponse output = new UpdatePropertiesResponse();
		try {
			ApiValidator.notNull(input, UpdatePropertiesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator
					.lookupAndExtend(input.session, "input.session");

			input.session.user = UserServiceProvider.provide()
					.getUser(Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, null,
					"input.session.user");

			List<Property> updatedProperties = PropertyValidator
					.validateAll(input.properties, "input.properties");

			Property existingProperty = null;
			boolean found;
			for (Property property : updatedProperties) {
				found = false;
				try {
					existingProperty = PropertyValidator.lookup(property,
							"input.properties[n]");
					found = true;
				} catch (InputValidationException ex) {
					LOG.info("Property [" + property.name
							+ "] does not exist. Will add with value ["
							+ property.value + "].");
				}

				if (found) {
					existingProperty.value = property.value;
					PropertyServiceProvider.provide()
							.updateProperty(existingProperty);
				} else {
					PropertyServiceProvider.provide().addProperty(property);
				}
			}

			UserHelper.stripPassword(
					output.session == null ? null : output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updateProperties");
		return output;
	}
}
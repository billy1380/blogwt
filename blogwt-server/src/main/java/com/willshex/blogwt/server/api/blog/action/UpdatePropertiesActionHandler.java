//  
//  UpdatePropertiesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class UpdatePropertiesActionHandler extends
		ActionHandler<UpdatePropertiesRequest, UpdatePropertiesResponse> {
	private static final Logger LOG = Logger
			.getLogger(UpdatePropertiesActionHandler.class.getName());
	@Override
	protected void handle (UpdatePropertiesRequest input,
			UpdatePropertiesResponse output) throws Exception {
		ApiValidator.request(input, UpdatePropertiesRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		UserValidator.authorisation(input.session.user, null,
				"input.session.user");

		List<Property> updatedProperties = PropertyValidator
				.validateAll(input.properties, "input.properties");

		Property existingProperty = null;
		boolean found;
		for (Property property : updatedProperties) {
			if (PropertyHelper.isBlankSecret(property)) {
				if (LOG.isLoggable(Level.INFO)) {
					LOG.info("Property [" + property.name
							+ "] is secret and empty. Not saving it.");
				}
				continue;
			}

			found = false;
			try {
				existingProperty = PropertyValidator.lookup(property,
						"input.properties[n]");
				found = true;
			} catch (InputValidationException ex) {
				if (LOG.isLoggable(Level.INFO)) {
					LOG.info("Property [" + property.name
							+ "] does not exist. Will add with value ["
							+ (PropertyHelper.isMaskedProperty(property)
									? "hidden" : property.value)
							+ "].");
				}
			}

			if (found) {
				existingProperty.value = property.value;
				PropertyServiceProvider.provide()
						.updateProperty(existingProperty);
			} else {
				PropertyServiceProvider.provide().addProperty(property);
			}
		}
	}
	@Override
	protected UpdatePropertiesResponse newOutput () {
		return new UpdatePropertiesResponse();
	}
	@Override
	protected Logger logger () {
		return LOG;
	}
}

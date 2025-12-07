// 
//  GetPropertiesActionHandler.java
//  blogwt
// 
//  Created by William Shakour on April 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.PropertySortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public final class GetPropertiesActionHandler
		extends ActionHandler<GetPropertiesRequest, GetPropertiesResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetPropertiesActionHandler.class.getName());

	@Override
	public void handle (GetPropertiesRequest input,
			GetPropertiesResponse output) throws Exception {
		ApiValidator.request(input, GetPropertiesRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		try {
			output.session = input.session = SessionValidator
					.lookupCheckAndExtend(input.session, "input.session");
		} catch (Exception e) {
			// session is not required for this call
		}

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		} else {
			if (input.pager.start == null) {
				input.pager.start = PagerHelper.DEFAULT_START;
			}

			if (input.pager.count == null) {
				input.pager.count = PagerHelper.DEFAULT_COUNT;
			}
		}

		output.properties = PropertyServiceProvider.provide().getProperties(
				input.pager.start, input.pager.count,
				PropertySortType.fromString(input.pager.sortBy),
				input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#clearSensitiveFields(
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	public void clearSensitiveFields (GetPropertiesResponse output) {
		super.clearSensitiveFields(output);

		if (output.properties != null) {
			Property property;
			for (int i = output.properties.size() - 1; i >= 0; i--) {
				property = output.properties.get(i);
				if (PropertyHelper.isSecretProperty(property)) {
					output.properties.remove(i);
				}
			}
		}
	}

	@Override
	protected GetPropertiesResponse newOutput () {
		return new GetPropertiesResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
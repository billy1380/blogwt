// 
//  GetCustomersActionHandler.java
//  rekoning
// 
//  Created by William Shakour on June 2, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.invoice.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.invoice.customer.CustomerServiceProvider;
import com.willshex.blogwt.server.service.invoice.customer.ICustomerService;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.datatype.invoice.CustomerSortType;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetCustomersActionHandler
		extends ActionHandler<GetCustomersRequest, GetCustomersResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetCustomersActionHandler.class.getName());

	@Override
	public void handle (GetCustomersRequest input, GetCustomersResponse output)
			throws Exception {
		input = ApiValidator.request(input, GetCustomersRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");
		output.session = input.session = SessionValidator.lookup(input.session,
				"input.session");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		CustomerSortType sortBy = null;
		if (input.pager.sortBy != null) {
			if ((sortBy = CustomerSortType
					.fromString(input.pager.sortBy)) == null)
				throw new InputValidationException(100024,
						"Invalid argument - CustomerSortBy: input.sortBy");
		}

		ICustomerService customerService = CustomerServiceProvider.provide();

		output.customers = customerService.getVendorCustomers(
				VendorServiceProvider.provide().getCurrentVendor(),
				input.pager.start, input.pager.count, sortBy,
				input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetCustomersResponse newOutput () {
		return new GetCustomersResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
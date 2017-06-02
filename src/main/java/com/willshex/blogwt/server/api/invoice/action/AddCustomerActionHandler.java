// 
//  AddCustomerActionHandler.java
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
import com.willshex.blogwt.server.service.invoice.address.AddressServiceProvider;
import com.willshex.blogwt.server.service.invoice.customer.CustomerServiceProvider;
import com.willshex.blogwt.server.service.invoice.customer.ICustomerService;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerRequest;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerResponse;

public final class AddCustomerActionHandler
		extends ActionHandler<AddCustomerRequest, AddCustomerResponse> {

	private static final Logger LOG = Logger
			.getLogger(AddCustomerActionHandler.class.getName());

	@Override
	public void handle (AddCustomerRequest input, AddCustomerResponse output)
			throws Exception {
		input = ApiValidator.request(input, AddCustomerRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");
		output.session = input.session = SessionValidator.lookup(input.session,
				"input.session");

		Vendor currentVendor = VendorServiceProvider.provide()
				.getCurrentVendor();
		if (input.customer != null && input.customer.vendorKey != null
				&& input.customer.vendorKey.getId() != currentVendor.id) {
			input.customer = null;
		} else {
			input.customer.vendor = currentVendor;
		}

		input.customer.address = AddressServiceProvider.provide()
				.addAddress(input.customer.address);

		ICustomerService customerService = CustomerServiceProvider.provide();
		customerService.addCustomer(input.customer);
	}

	@Override
	protected AddCustomerResponse newOutput () {
		return new AddCustomerResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
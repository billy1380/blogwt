// 
//  GetCurrentVendorActionHandler.java
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
import com.willshex.blogwt.server.service.invoice.vendor.IVendorService;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorResponse;

public final class GetCurrentVendorActionHandler extends
		ActionHandler<GetCurrentVendorRequest, GetCurrentVendorResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetCurrentVendorActionHandler.class.getName());

	@Override
	public void handle (GetCurrentVendorRequest input,
			GetCurrentVendorResponse output) throws Exception {
		input = ApiValidator.request(input, GetCurrentVendorRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");
		output.session = input.session = SessionValidator.lookup(input.session,
				"input.session");

		IVendorService vendorService = VendorServiceProvider.provide();
		output.vendor = vendorService.getCurrentVendor();
	}

	@Override
	protected GetCurrentVendorResponse newOutput () {
		return new GetCurrentVendorResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
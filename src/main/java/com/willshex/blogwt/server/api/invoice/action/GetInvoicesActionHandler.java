// 
//  GetInvoicesActionHandler.java
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
import com.willshex.blogwt.server.service.invoice.invoice.IInvoiceService;
import com.willshex.blogwt.server.service.invoice.invoice.InvoiceServiceProvider;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceSortType;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.server.InputValidationException;

public final class GetInvoicesActionHandler
		extends ActionHandler<GetInvoicesRequest, GetInvoicesResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetInvoicesActionHandler.class.getName());

	@Override
	public void handle (GetInvoicesRequest input, GetInvoicesResponse output)
			throws Exception {
		input = ApiValidator.request(input, GetInvoicesRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");
		output.session = input.session = SessionValidator.lookup(input.session,
				"input.session");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		if (input.pager.start == null || (input.pager.start.intValue() < 0))
			throw new InputValidationException(100003,
					"Invalid argument - int: input.start");

		if (input.pager.count == null || (input.pager.count.intValue() <= 0))
			throw new InputValidationException(100004,
					"Invalid argument - int: input.count");

		InvoiceSortType sortBy = null;
		if (input.pager.sortBy != null) {
			if ((sortBy = InvoiceSortType
					.fromString(input.pager.sortBy)) == null)
				throw new InputValidationException(100023,
						"Invalid argument - InvoiceSortBy: input.sortBy");
		}

		IInvoiceService invoiceService = InvoiceServiceProvider.provide();

		if (input.outstanding == null || input.outstanding == Boolean.FALSE) {
			output.invoices = invoiceService.getVendorInvoices(
					VendorServiceProvider.provide().getCurrentVendor(),
					input.pager.start, input.pager.count, sortBy,
					input.pager.sortDirection);
		} else {
			output.invoices = invoiceService.getVendorInvoices(
					VendorServiceProvider.provide().getCurrentVendor(),
					input.pager.start, input.pager.count, sortBy,
					input.pager.sortDirection);
		}

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetInvoicesResponse newOutput () {
		return new GetInvoicesResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
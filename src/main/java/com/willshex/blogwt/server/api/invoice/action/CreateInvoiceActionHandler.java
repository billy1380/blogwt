// 
//  CreateInvoiceActionHandler.java
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
import com.willshex.blogwt.server.service.invoice.currency.CurrencyServiceProvider;
import com.willshex.blogwt.server.service.invoice.customer.CustomerServiceProvider;
import com.willshex.blogwt.server.service.invoice.invoice.IInvoiceService;
import com.willshex.blogwt.server.service.invoice.invoice.InvoiceServiceProvider;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceRequest;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceResponse;

public final class CreateInvoiceActionHandler
		extends ActionHandler<CreateInvoiceRequest, CreateInvoiceResponse> {

	private static final Logger LOG = Logger
			.getLogger(CreateInvoiceActionHandler.class.getName());

	@Override
	public void handle (CreateInvoiceRequest input,
			CreateInvoiceResponse output) throws Exception {
		input = ApiValidator.request(input, CreateInvoiceRequest.class);
		input.accessCode = ApiValidator.accessCode(input.accessCode,
				"input.accessCode");
		output.session = input.session = SessionValidator.lookup(input.session,
				"input.session");

		IInvoiceService invoiceService = InvoiceServiceProvider.provide();
		input.invoice.vendor = VendorServiceProvider.provide()
				.getCurrentVendor();

		input.invoice.customer = CustomerServiceProvider.provide()
				.getCodeCustomer(input.invoice.customer.code);
		input.invoice.currency = CurrencyServiceProvider.provide()
				.getCodeCurrency(input.invoice.currency.code);

		invoiceService.addInvoice(input.invoice);
	}

	@Override
	protected CreateInvoiceResponse newOutput () {
		return new CreateInvoiceResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
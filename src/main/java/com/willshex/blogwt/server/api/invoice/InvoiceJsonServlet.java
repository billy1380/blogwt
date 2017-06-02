// 
//  InvoiceJsonServlet.java
//  rekoning
// 
//  Created by William Shakour on June 2, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.invoice;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.invoice.action.AddCustomerActionHandler;
import com.willshex.blogwt.server.api.invoice.action.CreateInvoiceActionHandler;
import com.willshex.blogwt.server.api.invoice.action.GetCurrenciesActionHandler;
import com.willshex.blogwt.server.api.invoice.action.GetCurrentVendorActionHandler;
import com.willshex.blogwt.server.api.invoice.action.GetCustomersActionHandler;
import com.willshex.blogwt.server.api.invoice.action.GetInvoicesActionHandler;
import com.willshex.blogwt.server.api.invoice.action.SetInvoiceStatusActionHandler;
import com.willshex.blogwt.server.api.invoice.action.SetupVendorActionHandler;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerRequest;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class InvoiceJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		if ("CreateInvoice".equals(action)) {
			CreateInvoiceRequest input = new CreateInvoiceRequest();
			input.fromJson(request);
			output = new CreateInvoiceActionHandler().handle(input).toString();
		} else if ("SetInvoiceStatus".equals(action)) {
			SetInvoiceStatusRequest input = new SetInvoiceStatusRequest();
			input.fromJson(request);
			output = new SetInvoiceStatusActionHandler().handle(input)
					.toString();
		} else if ("GetCurrentVendor".equals(action)) {
			GetCurrentVendorRequest input = new GetCurrentVendorRequest();
			input.fromJson(request);
			output = new GetCurrentVendorActionHandler().handle(input)
					.toString();
		} else if ("AddCustomer".equals(action)) {
			AddCustomerRequest input = new AddCustomerRequest();
			input.fromJson(request);
			output = new AddCustomerActionHandler().handle(input).toString();
		} else if ("SetupVendor".equals(action)) {
			SetupVendorRequest input = new SetupVendorRequest();
			input.fromJson(request);
			output = new SetupVendorActionHandler().handle(input).toString();
		} else if ("GetCurrencies".equals(action)) {
			GetCurrenciesRequest input = new GetCurrenciesRequest();
			input.fromJson(request);
			output = new GetCurrenciesActionHandler().handle(input).toString();
		} else if ("GetCustomers".equals(action)) {
			GetCustomersRequest input = new GetCustomersRequest();
			input.fromJson(request);
			output = new GetCustomersActionHandler().handle(input).toString();
		} else if ("GetInvoices".equals(action)) {
			GetInvoicesRequest input = new GetInvoicesRequest();
			input.fromJson(request);
			output = new GetInvoicesActionHandler().handle(input).toString();
		}
		return output;
	}
}
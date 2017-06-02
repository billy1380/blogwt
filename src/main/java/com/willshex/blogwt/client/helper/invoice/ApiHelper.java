//
//  ApiHelper.java
//  willshex-reckoning
//
//  Created by William Shakour (billy1380) on 2 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper.invoice;

import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.InvoiceService;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ApiHelper extends com.willshex.blogwt.client.helper.ApiHelper {

	public static final String INVOICE_END_POINT = BASE_URL + "invoice";

	public static InvoiceService createInvoiceClient() {
		InvoiceService service = new InvoiceService();
		service.setUrl(INVOICE_END_POINT);
		service.setBus(DefaultEventBus.get());
		return service;
	}
}

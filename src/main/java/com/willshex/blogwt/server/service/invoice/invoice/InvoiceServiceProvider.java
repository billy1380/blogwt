//  
//  InvoiceServiceProvider.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.invoice;

import com.willshex.service.ServiceDiscovery;

final public class InvoiceServiceProvider {

	/**
	 * @return
	 */
	public static IInvoiceService provide() {
		IInvoiceService invoiceService = null;

		if ((invoiceService = (IInvoiceService) ServiceDiscovery.getService(IInvoiceService.NAME)) == null) {
			invoiceService = InvoiceServiceFactory.createNewInvoiceService();
			ServiceDiscovery.registerService(invoiceService);
		}

		return invoiceService;
	}

}
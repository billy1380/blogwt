//  
//  InvoiceServiceFactory.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.invoice;

final class InvoiceServiceFactory {

	/**
	* @return
	*/
	public static IInvoiceService createNewInvoiceService () {
		return new InvoiceService();
	}

}
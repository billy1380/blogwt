//  
//  CurrencyServiceFactory.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.currency;

final class CurrencyServiceFactory {

	/**
	 * @return
	 */
	public static ICurrencyService createNewCurrencyService () {
		return new CurrencyService();
	}

}
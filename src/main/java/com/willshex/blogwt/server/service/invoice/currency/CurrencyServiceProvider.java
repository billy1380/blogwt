//  
//  CurrencyServiceProvider.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.currency;

import com.willshex.service.ServiceDiscovery;

final public class CurrencyServiceProvider {

	/**
	 * @return
	 */
	public static ICurrencyService provide () {
		ICurrencyService currencyService = null;

		if ((currencyService = (ICurrencyService) ServiceDiscovery
				.getService(ICurrencyService.NAME)) == null) {
			currencyService = CurrencyServiceFactory.createNewCurrencyService();
			ServiceDiscovery.registerService(currencyService);
		}

		return currencyService;
	}

}
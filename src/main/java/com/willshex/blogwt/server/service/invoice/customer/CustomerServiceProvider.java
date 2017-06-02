//  
//  CustomerServiceProvider.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.customer;

import com.willshex.service.ServiceDiscovery;

final public class CustomerServiceProvider {

	/**
	 * @return
	 */
	public static ICustomerService provide () {
		ICustomerService customerService = null;

		if ((customerService = (ICustomerService) ServiceDiscovery
				.getService(ICustomerService.NAME)) == null) {
			customerService = CustomerServiceFactory.createNewCustomerService();
			ServiceDiscovery.registerService(customerService);
		}

		return customerService;
	}

}
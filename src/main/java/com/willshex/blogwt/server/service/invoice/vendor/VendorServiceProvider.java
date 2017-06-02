//  
//  VendorServiceProvider.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.vendor;

import com.willshex.service.ServiceDiscovery;

final public class VendorServiceProvider {

	/**
	 * @return
	 */
	public static IVendorService provide () {
		IVendorService vendorService = null;

		if ((vendorService = (IVendorService) ServiceDiscovery
				.getService(IVendorService.NAME)) == null) {
			vendorService = VendorServiceFactory.createNewVendorService();
			ServiceDiscovery.registerService(vendorService);
		}

		return vendorService;
	}

}
//  
//  AddressServiceProvider.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.address;

import com.willshex.service.ServiceDiscovery;

final public class AddressServiceProvider {

	/**
	 * @return
	 */
	public static IAddressService provide () {
		IAddressService addressService = null;

		if ((addressService = (IAddressService) ServiceDiscovery
				.getService(IAddressService.NAME)) == null) {
			addressService = AddressServiceFactory.createNewAddressService();
			ServiceDiscovery.registerService(addressService);
		}

		return addressService;
	}

}
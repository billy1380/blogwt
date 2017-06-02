//  
//  VendorServiceFactory.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.vendor;

final class VendorServiceFactory {

	/**
	 * @return
	 */
	public static IVendorService createNewVendorService () {
		return new VendorService();
	}

}
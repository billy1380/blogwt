//  
//  IAddressService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.address;

import com.willshex.blogwt.shared.api.datatype.invoice.Address;
import com.willshex.service.IService;

public interface IAddressService extends IService {

	public static final String NAME = "quickinvoice.address";

	/**
	 * @param id
	 * @return
	 */
	public Address getAddress (Long id);

	/**
	 * @param address
	 * @return
	 */
	public Address addAddress (Address address);

	/**
	 * @param address
	 * @return
	 */
	public Address updateAddress (Address address);

	/**
	 * @param address
	 */
	public void deleteAddress (Address address);

}
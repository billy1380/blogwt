//  
//  AddressService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.address;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.shared.api.datatype.invoice.Address;

final class AddressService implements IAddressService {

	public String getName () {
		return IAddressService.NAME;
	}

	public Address getAddress (Long id) {
		return load().id(id).now();
	}

	/**
	 * @return
	 */
	private LoadType<Address> load () {
		return provide().load().type(Address.class);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.address.
	 * IAddressService#addAddress(com.spacehopperstudios.quickinvoice.shared.api
	 * .datatypes.Address) */
	@Override
	public Address addAddress (Address address) {
		if (address.created == null) {
			address.created = new Date();
		}
		Key<Address> key = provide().save().entity(address).now();
		address.id = Long.valueOf(key.getId());
		return address;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.address.
	 * IAddressService#updateAddress(com.spacehopperstudios.quickinvoice.shared.
	 * api.datatypes. Address ) */
	@Override
	public Address updateAddress (Address address) {
		provide().save().entity(address).now();
		return address;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.address.
	 * IAddressService#deleteAddress(com.spacehopperstudios.quickinvoice.shared.
	 * api.datatypes. Address ) */
	@Override
	public void deleteAddress (Address address) {
		provide().delete().entity(address).now();

	}

}
//  
//  VendorService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.vendor;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.willshex.blogwt.server.service.invoice.address.AddressServiceProvider;
import com.willshex.blogwt.server.service.invoice.bankaccount.BankAccountServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.server.ContextAwareServlet;

final class VendorService implements IVendorService {

	public String getName () {
		return NAME;
	}

	public Vendor getVendor (Long id) {
		return provide().load().type(Vendor.class).id(id.longValue()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #
	 * addVendor(com.spacehopperstudios.quickinvoice.shared.api.datatypes.Vendor
	 * ) */
	@Override
	public Vendor addVendor (Vendor vendor) {
		if (vendor.created == null) {
			vendor.created = new Date();
		}

		vendor.addressKey = Key.create(vendor.address);
		if (vendor.logo != null) {
			vendor.logoKey = Key.create(vendor.logo);
		}

		Key<Vendor> key = provide().save().entity(vendor).now();
		vendor.id = Long.valueOf(key.getId());

		String hostName = getHostName();
		PropertyServiceProvider.provide()
				.addProperty(new Property().name(hostName).group("Landing page")
						.value(Long.toString(vendor.id))
						.description("Reconing vendor on " + hostName)
						.type("system"));

		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * # updateVendor(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Vendor ) */
	@Override
	public Vendor updateVendor (Vendor vendor) {
		if (vendor.logo != null) {
			vendor.logoKey = Key.create(vendor.logo);
		}

		provide().save().entity(vendor).now();
		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * # deleteVendor(com.spacehopperstudios.quickinvoice.shared.api.datatypes.
	 * Vendor ) */
	@Override
	public void deleteVendor (Vendor vendor) {
		provide().delete().entity(vendor).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #getCurrentVendor() */
	@Override
	public Vendor getCurrentVendor () {
		Long vendorId = Long.valueOf(PropertyServiceProvider.provide()
				.getNamedProperty(getHostName()).value);

		Vendor vendor = getVendor(vendorId);
		vendor.accounts = BankAccountServiceProvider.provide()
				.getVendorBankAccounts(vendor);

		if (vendor.addressKey != null) {
			vendor.address = AddressServiceProvider.provide()
					.getAddress(Long.valueOf(vendor.addressKey.getId()));
		}

		if (vendor.logoKey != null) {
			vendor.logo = ResourceServiceProvider.provide()
					.getResource(Long.valueOf(vendor.logoKey.getId()));
		}

		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #incrementCustomers() */
	@Override
	public Vendor incrementCustomers (Vendor vendor) {
		return incrementCustomers(vendor, Integer.valueOf(1));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #incrementCustomers(int) */
	@Override
	public Vendor incrementCustomers (final Vendor vendor,
			final Integer increment) {
		Vendor u = provide().transact(new Work<Vendor>() {

			@Override
			public Vendor run () {
				Vendor l = getVendor(vendor.id);
				l.customerCount = Integer.valueOf((l.customerCount == null ? 0
						: l.customerCount.intValue()) + increment);
				return updateVendor(l);
			}

		});

		vendor.customerCount = u.customerCount;

		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #incrementInvoices() */
	@Override
	public Vendor incrementInvoices (Vendor vendor) {
		return incrementInvoices(vendor, Integer.valueOf(1));

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #incrementInvoices(int) */
	@Override
	public Vendor incrementInvoices (final Vendor vendor,
			final Integer increment) {
		Vendor u = provide().transact(new Work<Vendor>() {

			@Override
			public Vendor run () {
				Vendor l = getVendor(vendor.id);
				l.invoiceCount = Integer.valueOf(
						(l.invoiceCount == null ? 0 : l.invoiceCount.intValue())
								+ increment);
				return updateVendor(l);
			}

		});

		vendor.invoiceCount = u.invoiceCount;

		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * # incrementOutstanding(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Vendor) */
	@Override
	public Vendor incrementOutstanding (Vendor vendor) {
		return incrementOutstanding(vendor, Integer.valueOf(1));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * # incrementOutstanding(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Vendor, java.lang.Integer) */
	@Override
	public Vendor incrementOutstanding (final Vendor vendor,
			final Integer increment) {
		Vendor u = provide().transact(new Work<Vendor>() {

			@Override
			public Vendor run () {
				Vendor l = getVendor(vendor.id);
				l.outstandingCount = Integer.valueOf((l.outstandingCount == null
						? 0 : l.outstandingCount.intValue()) + increment);
				return updateVendor(l);
			}

		});

		vendor.outstandingCount = u.outstandingCount;

		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.server.services.vendor.IVendorService
	 * #getHostName() */
	@Override
	public String getHostName () {
		String hostName = ContextAwareServlet.REQUEST.get().getServerName();
		int port = ContextAwareServlet.REQUEST.get().getServerPort();

		if (port != 80) {
			hostName += ":" + port;
		}

		return hostName;
	}
}
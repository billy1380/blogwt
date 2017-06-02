//  
//  CustomerService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.customer;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.invoice.address.AddressServiceProvider;
import com.willshex.blogwt.server.service.invoice.address.IAddressService;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;
import com.willshex.blogwt.shared.api.datatype.invoice.CustomerSortType;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;

final class CustomerService implements ICustomerService {

	public String getName () {
		return NAME;
	}

	public Customer getCustomer (Long id) {
		return provide().load().type(Customer.class).id(id.longValue()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * ICustomerService#addCustomer(com.spacehopperstudios.quickinvoice.server
	 * .services.customer .Customer) */
	@Override
	public Customer addCustomer (Customer customer) {
		if (customer.created == null) {
			customer.created = new Date();
		}

		customer.addressKey = Key.create(customer.address);
		customer.vendorKey = Key.create(customer.vendor);

		customer.outstandingCount = Integer.valueOf(0);
		customer.invoiceCount = Integer.valueOf(0);

		if (customer.phone == null) {
			customer.phone = "-";
		}

		Key<Customer> key = provide().save().entity(customer).now();
		customer.id = Long.valueOf(key.getId());

		VendorServiceProvider.provide().incrementCustomers(customer.vendor);

		return customer;
	}

	/* (non-Javadoc)
	 * 
	 * @see ICustomerService#updateCustomer(com.spacehopperstudios.quickinvoice.
	 * server .services.customer .Customer) */
	@Override
	public Customer updateCustomer (Customer customer) {
		provide().save().entity(customer);
		return customer;
	}

	/* (non-Javadoc)
	 * 
	 * @see ICustomerService#deleteCustomer(com.spacehopperstudios.quickinvoice.
	 * server .services.customer .Customer) */
	@Override
	public void deleteCustomer (Customer customer) {
		provide().delete().entity(customer).now();
		VendorServiceProvider.provide().incrementCustomers(customer.vendor,
				Integer.valueOf(-1));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService #
	 * getVendorCustomers(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Vendor, java.lang.Long, java.lang.Long,
	 * com.spacehopperstudios.quickinvoice
	 * .shared.api.datatypes.CustomerSortType,
	 * com.spacehopperstudios.quickinvoice.shared.api.SortDirectionType) */
	@Override
	public List<Customer> getVendorCustomers (Vendor vendor, Integer start,
			Integer count, CustomerSortType sortBy,
			SortDirectionType sortDirection) {
		List<Customer> customers = null;

		Query<Customer> query = provide().load().type(Customer.class)
				.filter("vendorKey", vendor);

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		customers = query.list();

		if (customers != null) {
			IAddressService addressService = AddressServiceProvider.provide();
			for (Customer currentCustomer : customers) {
				if (currentCustomer.addressKey != null) {
					currentCustomer.address = addressService.getAddress(
							Long.valueOf(currentCustomer.addressKey.getId()));
				}
			}
		}

		return customers;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService
	 * #getVendorCustomersCount(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes.Vendor) */
	@Override
	public Integer getVendorCustomersCount (Vendor vendor) {
		return vendor.customerCount;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService #
	 * incrementInvoices(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Customer) */
	@Override
	public Customer incrementInvoices (Customer customer) {
		return incrementInvoices(customer, Integer.valueOf(1));

	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService #
	 * incrementInvoices(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Customer, java.lang.Integer) */
	@Override
	public Customer incrementInvoices (final Customer customer,
			final Integer increment) {
		Customer u = provide().transact(new Work<Customer>() {

			@Override
			public Customer run () {
				Customer l = getCustomer(customer.id);
				l.invoiceCount = Integer.valueOf(
						(l.invoiceCount == null ? 0 : l.invoiceCount.intValue())
								+ increment);
				updateCustomer(l);
				return l;
			}

		});

		customer.invoiceCount = u.invoiceCount;

		return customer;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService #getCodeCustomer(java.lang.String) */
	@Override
	public Customer getCodeCustomer (String code) {
		Key<Customer> key = provide().load().type(Customer.class)
				.filter("code", code).limit(1).keys().first().now();
		return getCustomer(Long.valueOf(key.getId()));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService #
	 * incrementOutstanding(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Customer) */
	@Override
	public Customer incrementOutstanding (Customer customer) {
		return incrementOutstanding(customer, Integer.valueOf(1));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.server.services.customer.
	 * ICustomerService #
	 * incrementOutstanding(com.spacehopperstudios.quickinvoice.shared.api.
	 * datatypes .Customer, java.lang.Integer) */
	@Override
	public Customer incrementOutstanding (final Customer customer,
			final Integer increment) {
		Customer u = provide().transact(new Work<Customer>() {

			@Override
			public Customer run () {
				Customer l = getCustomer(customer.id);
				l.outstandingCount = Integer.valueOf((l.outstandingCount == null
						? 0 : l.outstandingCount.intValue()) + increment);
				updateCustomer(l);
				return l;
			}

		});

		customer.outstandingCount = u.outstandingCount;

		return customer;
	}

}
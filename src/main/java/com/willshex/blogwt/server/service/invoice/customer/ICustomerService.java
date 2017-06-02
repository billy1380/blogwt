//  
//  ICustomerService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.customer;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;
import com.willshex.blogwt.shared.api.datatype.invoice.CustomerSortType;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.service.IService;

public interface ICustomerService extends IService {
	public static final String NAME = "quickinvoice.customer";

	/**
	 * @param id
	 * @return
	 */
	public Customer getCustomer (Long id);

	/**
	 * @param customer
	 * @return
	 */
	public Customer addCustomer (Customer customer);

	/**
	 * @param customer
	 * @return
	 */
	public Customer updateCustomer (Customer customer);

	/**
	 * @param customer
	 */
	public void deleteCustomer (Customer customer);

	/**
	 * @param currentVendor
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Customer> getVendorCustomers (Vendor currentVendor, Long start,
			Long count, CustomerSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * @param currentVendor
	 * @return
	 */
	public Integer getVendorCustomersCount (Vendor currentVendor);

	/**
	 * @param customer
	 */
	public Customer incrementInvoices (Customer customer);

	/**
	 * @param customer
	 */
	public Customer incrementInvoices (Customer customer, Integer increment);

	/**
	 * @param customer
	 */
	public Customer incrementOutstanding (Customer customer);

	/**
	 * @param customer
	 */
	public Customer incrementOutstanding (Customer customer, Integer increment);

	/**
	 * @param code
	 * @return
	 */
	public Customer getCodeCustomer (String code);

}
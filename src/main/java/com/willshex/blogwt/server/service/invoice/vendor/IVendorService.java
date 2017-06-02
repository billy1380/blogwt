//  
//  IVendorService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.vendor;

import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.service.IService;

public interface IVendorService extends IService {
	public static final String NAME = "quickinvoice.vendor";

	/**
	 * @param id
	 * @return
	 */
	public Vendor getVendor (Long id);

	/**
	 * @param vendor
	 * @return
	 */
	public Vendor addVendor (Vendor vendor);

	/**
	 * @param vendor
	 * @return
	 */
	public Vendor updateVendor (Vendor vendor);

	/**
	 * @param vendor
	 */
	public void deleteVendor (Vendor vendor);

	/**
	 * @return
	 */
	public Vendor getCurrentVendor ();

	/**
	 * 
	 * @param vendor
	 * @return
	 */
	public Vendor incrementCustomers (Vendor vendor);

	/**
	 * 
	 * @param vendor
	 * @param increment
	 * @return
	 */
	public Vendor incrementCustomers (Vendor vendor, Integer increment);

	/**
	 * 
	 * @param vendor
	 * @return
	 */
	public Vendor incrementInvoices (Vendor vendor);

	/**
	 * 
	 * @param vendor
	 * @param increment
	 * @return
	 */
	public Vendor incrementInvoices (Vendor vendor, Integer increment);

	/**
	 * 
	 * @param vendor
	 * @return
	 */
	public Vendor incrementOutstanding (Vendor vendor);

	/**
	 * 
	 * @param vendor
	 * @param increment
	 * @return
	 */
	public Vendor incrementOutstanding (Vendor vendor, Integer increment);

	/**
	 * 
	 * @return
	 */
	public String getHostName ();

}
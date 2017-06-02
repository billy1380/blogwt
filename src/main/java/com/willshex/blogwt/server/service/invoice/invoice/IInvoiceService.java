//  
//  IInvoiceService.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.invoice;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceSortType;
import com.willshex.blogwt.shared.api.datatype.invoice.Item;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.service.IService;

public interface IInvoiceService extends IService {

	public static final String NAME = "quickinvoice.invoice";

	/**
	 * @param id
	 * @return
	 */
	public Invoice getInvoice (Long id);

	/**
	 * @param invoice
	 * @return
	 */
	public Invoice addInvoice (Invoice invoice);

	/**
	 * @param invoice
	 * @param b
	 * @return
	 */
	public Invoice updateInvoice (Invoice invoice, Boolean decrementCount);

	/**
	 * @param invoice
	 */
	public void deleteInvoice (Invoice invoice);

	/**
	 * @param currentVendor
	 * @return
	 */
	public Integer getVendorInvoicesCount (Vendor currentVendor);

	/**
	 * @param currentVendor
	 * @return
	 */
	public Integer getVendorOutstandingInvoicesCount (Vendor currentVendor);

	/**
	 * @param currentVendor
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Invoice> getVendorInvoices (Vendor vendor, Long start,
			Long count, InvoiceSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * @param currentVendor
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Invoice> getVendorOutstandingInvoices (Vendor vendor,
			Long start, Long count, InvoiceSortType sortBy,
			SortDirectionType sortDirection);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Item getItem (Long id);

	/**
	 * 
	 * @param invoice
	 * @param item
	 * @return
	 */
	public Item addInvoiceItem (Invoice invoice, Item item);

	/**
	 * Update item
	 * 
	 * @param item
	 * @return
	 */
	public Item updateInvoiceItem (Invoice invoice, Item item);

	/**
	 * 
	 * @param invoice
	 * @return
	 */
	public List<Item> getInvoiceItems (Invoice invoice);

	/**
	 * @param invoice
	 * @return
	 */
	public Invoice incrementItems (Invoice invoice);

	public Invoice incrementItems (Invoice invoice, Integer increment);

	/**
	 * @param invoice
	 * @param increment
	 * @return
	 */
	public Invoice incrementPrice (Invoice invoice, Integer increment);

	public Invoice getNamedInvoice (String name);

}
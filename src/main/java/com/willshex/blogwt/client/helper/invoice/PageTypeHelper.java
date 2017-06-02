//
//  PageTypeHelper.java
//  willshex-reckoning
//
//  Created by William Shakour (billy1380) on 2 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper.invoice;

import com.google.gwt.safehtml.shared.SafeUri;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.invoice.Account;
import com.willshex.blogwt.client.page.invoice.AddCustomer;
import com.willshex.blogwt.client.page.invoice.AddInvoice;
import com.willshex.blogwt.client.page.invoice.Customers;
import com.willshex.blogwt.client.page.invoice.Invoices;
import com.willshex.blogwt.client.page.invoice.SetupVendor;
import com.willshex.blogwt.client.page.invoice.Vendor;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageTypeHelper
		extends com.willshex.blogwt.client.helper.PageTypeHelper {

	public static final SafeUri ADD_CUSTOMER_HREF = asHref(
			PageType.AddCustomerPageType);

	public static final SafeUri ADD_INVOICE_HREF = asHref(
			PageType.AddInvoicePageType);

	public static Page createPage (PageType pageType) {
		Page page = null;

		switch (pageType) {
		case AccountPageType:
			page = new Account();
			break;
		case CustomersPageType:
			page = new Customers();
			break;
		case InvoicesPageType:
			page = new Invoices();
			break;
		case AddInvoicePageType:
			page = new AddInvoice();
			break;
		case AddCustomerPageType:
			page = new AddCustomer();
			break;
		case VendorPageType:
			page = new Vendor();
			break;
		case SetupVendorPageType:
			page = new SetupVendor();
			break;
		default:
			page = com.willshex.blogwt.client.helper.PageTypeHelper
					.createPage(pageType);
			break;
		}

		return page;
	}
}

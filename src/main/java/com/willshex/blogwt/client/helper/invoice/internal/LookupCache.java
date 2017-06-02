//
//  LookupCache.java
//  quickinvoice
//
//  Created by billy1380 on 1 Aug 2013.
//  Copyright © 2013 Spacehopper Studios Ltd. All rights reserved.
//

package com.willshex.blogwt.client.helper.invoice.internal;

import java.util.HashMap;
import java.util.Map;

import com.willshex.blogwt.client.controller.invoice.CurrencyController;
import com.willshex.blogwt.client.controller.invoice.CustomerController;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;

/**
 * @author billy1380
 * 
 */
public class LookupCache {

	public static Map<String, Customer> customers = new HashMap<String, Customer>();
	public static Map<String, Currency> currencies = new HashMap<String, Currency>();

	static {
		CurrencyController.get();
		CustomerController.get();
	}

}

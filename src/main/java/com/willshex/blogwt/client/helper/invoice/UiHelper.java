//
//  UiHelper.java
//  willshex-reckoning
//
//  Created by William Shakour (billy1380) on 2 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper.invoice;

import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;
import com.willshex.blogwt.client.helper.invoice.internal.LookupCache;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UiHelper extends com.willshex.blogwt.client.helper.UiHelper {
	public static void addCustomersFromCache (ListBox widget) {
		widget.clear();

		Map<String, Customer> customers = LookupCache.customers;

		for (String key : customers.keySet()) {
			widget.addItem(customers.get(key).name, key);
		}
	}

	public static void addCurrenciesFromCache (ListBox widget) {
		widget.clear();

		Map<String, Currency> currencies = LookupCache.currencies;

		for (String key : currencies.keySet()) {
			widget.addItem(currencies.get(key).description, key);
		}
	}

	public static void addUnits (ListBox widget) {
		widget.clear();
		widget.addItem("None", "");
		widget.addItem("Hours", "h");
		widget.addItem("Days", "d");
		widget.addItem("Weeks", "w");
		widget.addItem("Months", "m");
	}
}

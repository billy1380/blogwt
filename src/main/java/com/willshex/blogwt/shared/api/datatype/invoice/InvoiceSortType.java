//  
//  InvoiceSortType.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.HashMap;
import java.util.Map;

public enum InvoiceSortType {
	InvoiceSortTypeId("id"),
	InvoiceSortTypeVendor("vendor"),
	InvoiceSortTypeCustomer("customer"),
	InvoiceSortTypeDate("date"),
	InvoiceSortTypeTax("tax"),
	InvoiceSortTypeDeposit("deposit"),
	InvoiceSortTypeCurrency("currency"), ;
	private String value;
	private static Map<String, InvoiceSortType> valueLookup = null;

	public String toString() {
		return value;
	}

	private InvoiceSortType(String value) {
		this.value = value;
	}

	public static InvoiceSortType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, InvoiceSortType>();
			for (InvoiceSortType currentInvoiceSortType : InvoiceSortType.values()) {
				valueLookup.put(currentInvoiceSortType.value, currentInvoiceSortType);
			}
		}
		return valueLookup.get(value);
	}
}
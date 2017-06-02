//  
//  CustomerSortType.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.HashMap;
import java.util.Map;

public enum CustomerSortType {
	CustomerSortTypeId("id"),
	CustomerSortTypeVendor("vendor"),
	CustomerSortTypeName("name"),
	CustomerSortTypeCode("code"),
	CustomerSortTypeDescription("description"), ;
	private String value;
	private static Map<String, CustomerSortType> valueLookup = null;

	public String toString() {
		return value;
	}

	private CustomerSortType(String value) {
		this.value = value;
	}

	public static CustomerSortType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, CustomerSortType>();
			for (CustomerSortType currentCustomerSortType : CustomerSortType.values()) {
				valueLookup.put(currentCustomerSortType.value, currentCustomerSortType);
			}
		}
		return valueLookup.get(value);
	}
}
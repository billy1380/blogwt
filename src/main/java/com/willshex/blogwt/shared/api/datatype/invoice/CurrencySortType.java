//  
//  CurrencySortType.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.HashMap;
import java.util.Map;

public enum CurrencySortType {
	CurrencySortTypeId("id"),
	CurrencySortTypeCode("code"),
	CurrencySortTypeDescription("description"), ;
	private String value;
	private static Map<String, CurrencySortType> valueLookup = null;

	public String toString() {
		return value;
	}

	private CurrencySortType(String value) {
		this.value = value;
	}

	public static CurrencySortType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, CurrencySortType>();
			for (CurrencySortType currentCurrencySortType : CurrencySortType.values()) {
				valueLookup.put(currentCurrencySortType.value, currentCurrencySortType);
			}
		}
		return valueLookup.get(value);
	}
}
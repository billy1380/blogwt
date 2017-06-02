//  
//  CurrencyPossitionType.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.HashMap;
import java.util.Map;

public enum CurrencyPossitionType {
	CurrencyPossitionTypeBefore("before"),
	CurrencyPossitionTypeAfter("after"), ;
	private String value;
	private static Map<String, CurrencyPossitionType> valueLookup = null;

	public String toString() {
		return value;
	}

	private CurrencyPossitionType(String value) {
		this.value = value;
	}

	public static CurrencyPossitionType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, CurrencyPossitionType>();
			for (CurrencyPossitionType currentCurrencyPossitionType : CurrencyPossitionType.values()) {
				valueLookup.put(currentCurrencyPossitionType.value, currentCurrencyPossitionType);
			}
		}
		return valueLookup.get(value);
	}
}
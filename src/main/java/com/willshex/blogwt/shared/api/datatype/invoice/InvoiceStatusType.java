//  
//  InvoiceStatusType.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.HashMap;
import java.util.Map;

public enum InvoiceStatusType {
	InvoiceStatusTypeCreated("created"),
	InvoiceStatusTypeIssued("issued"),
	InvoiceStatusTypePaid("paid"),
	InvoiceStatusTypeRefunded("refunded"), ;
	private String value;
	private static Map<String, InvoiceStatusType> valueLookup = null;

	public String toString() {
		return value;
	}

	private InvoiceStatusType(String value) {
		this.value = value;
	}

	public static InvoiceStatusType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, InvoiceStatusType>();
			for (InvoiceStatusType currentInvoiceStatusType : InvoiceStatusType.values()) {
				valueLookup.put(currentInvoiceStatusType.value, currentInvoiceStatusType);
			}
		}
		return valueLookup.get(value);
	}
}
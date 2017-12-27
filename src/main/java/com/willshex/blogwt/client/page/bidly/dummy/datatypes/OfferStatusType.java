// 
//  OfferStatusType.java
//  bidly
// 
//  Created by William Shakour on December 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.page.bidly.dummy.datatypes;

import java.util.HashMap;
import java.util.Map;

public enum OfferStatusType {
	OfferStatusTypeOpen("open"),
	OfferStatusTypeClosed("closed"),
	OfferStatusTypeAccepted("accepted"),;
	private String value;
	private static Map<String, OfferStatusType> valueLookup = null;

	public String toString () {
		return value;
	}

	private OfferStatusType (String value) {
		this.value = value;
	}

	public static OfferStatusType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, OfferStatusType>();
			for (OfferStatusType currentOfferStatusType : OfferStatusType
					.values()) {
				valueLookup.put(currentOfferStatusType.value,
						currentOfferStatusType);
			}
		}
		return valueLookup.get(value);
	}
}
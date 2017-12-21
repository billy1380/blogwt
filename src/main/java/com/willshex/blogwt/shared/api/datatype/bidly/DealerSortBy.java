// 
//  DealerSortBy.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype.bidly;

import java.util.HashMap;
import java.util.Map;

public enum DealerSortBy {
	DealerSortById("id"),
	DealerSortByCreated("created"),
	DealerSortByName("name"),;
	private String value;
	private static Map<String, DealerSortBy> valueLookup = null;

	public String toString () {
		return value;
	}

	private DealerSortBy (String value) {
		this.value = value;
	}

	public static DealerSortBy fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, DealerSortBy>();
			for (DealerSortBy currentDealerSortBy : DealerSortBy.values()) {
				valueLookup.put(currentDealerSortBy.value, currentDealerSortBy);
			}
		}
		return valueLookup.get(value);
	}
}
// 
//  BranchSortBy.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype.bidly;

import java.util.HashMap;
import java.util.Map;

public enum BranchSortBy {
	BranchSortById("id"),
	BranchSortByCreated("created"),
	BranchSortByDealer("dealer"),;
	private String value;
	private static Map<String, BranchSortBy> valueLookup = null;

	public String toString () {
		return value;
	}

	private BranchSortBy (String value) {
		this.value = value;
	}

	public static BranchSortBy fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, BranchSortBy>();
			for (BranchSortBy currentBranchSortBy : BranchSortBy.values()) {
				valueLookup.put(currentBranchSortBy.value, currentBranchSortBy);
			}
		}
		return valueLookup.get(value);
	}
}
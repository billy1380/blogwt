//  
//  RatingSortType.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum RatingSortType {
	RatingSortTypeSubjectId("subjectId"),
	RatingSortTypeSubjectType("subjectType"),
	RatingSortTypeBy("by"),;
	private String value;
	private static Map<String, RatingSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private RatingSortType (String value) {
		this.value = value;
	}

	public static RatingSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, RatingSortType>();
			for (RatingSortType currentRatingSortType : RatingSortType
					.values()) {
				valueLookup.put(currentRatingSortType.value,
						currentRatingSortType);
			}
		}
		return valueLookup.get(value);
	}
}
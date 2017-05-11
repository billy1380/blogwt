// 
//  PushTokenSortType.java
//  blogwt
// 
//  Created by William Shakour on May 11, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum PushTokenSortType {
	PushTokenSortTypeId("id"),
	PushTokenSortTypeCreated("created"),
	PushTokenSortTypeUser("user"),
	PushTokenSortTypePlatform("platform"),;
	private String value;
	private static Map<String, PushTokenSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private PushTokenSortType (String value) {
		this.value = value;
	}

	public static PushTokenSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PushTokenSortType>();
			for (PushTokenSortType currentPushTokenSortType : PushTokenSortType
					.values()) {
				valueLookup.put(currentPushTokenSortType.value,
						currentPushTokenSortType);
			}
		}
		return valueLookup.get(value);
	}
}
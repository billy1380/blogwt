// 
//  MetaNotificationSortType.java
//  blogwt
// 
//  Created by William Shakour on May 11, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum MetaNotificationSortType {
	MetaNotificationSortTypeId("id"),
	MetaNotificationSortTypeCreated("created"),
	MetaNotificationSortTypeCode("code"),;
	private String value;
	private static Map<String, MetaNotificationSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private MetaNotificationSortType (String value) {
		this.value = value;
	}

	public static MetaNotificationSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, MetaNotificationSortType>();
			for (MetaNotificationSortType currentMetaNotificationSortType : MetaNotificationSortType
					.values()) {
				valueLookup.put(currentMetaNotificationSortType.value,
						currentMetaNotificationSortType);
			}
		}
		return valueLookup.get(value);
	}
}
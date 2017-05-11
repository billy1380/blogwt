// 
//  NotificationSortType.java
//  blogwt
// 
//  Created by William Shakour on May 11, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum NotificationSortType {
	NotificationSortTypeId("id"),
	NotificationSortTypeCreated("created"),
	NotificationSortTypeTarget("target"),
	NotificationSortTypeMode("mode"),;
	private String value;
	private static Map<String, NotificationSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private NotificationSortType (String value) {
		this.value = value;
	}

	public static NotificationSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, NotificationSortType>();
			for (NotificationSortType currentNotificationSortType : NotificationSortType
					.values()) {
				valueLookup.put(currentNotificationSortType.value,
						currentNotificationSortType);
			}
		}
		return valueLookup.get(value);
	}
}
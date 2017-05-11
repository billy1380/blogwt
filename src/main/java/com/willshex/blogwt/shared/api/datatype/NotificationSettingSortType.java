// 
//  NotificationSettingSortType.java
//  blogwt
// 
//  Created by William Shakour on May 11, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum NotificationSettingSortType {
	NotificationSettingSortTypeId("id"),
	NotificationSettingSortTypeCreated("created"),
	NotificationSettingSortTypeUser("user"),
	NotificationSettingSortTypeMeta("meta"),;
	private String value;
	private static Map<String, NotificationSettingSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private NotificationSettingSortType (String value) {
		this.value = value;
	}

	public static NotificationSettingSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, NotificationSettingSortType>();
			for (NotificationSettingSortType currentNotificationSettingSortType : NotificationSettingSortType
					.values()) {
				valueLookup.put(currentNotificationSettingSortType.value,
						currentNotificationSettingSortType);
			}
		}
		return valueLookup.get(value);
	}
}
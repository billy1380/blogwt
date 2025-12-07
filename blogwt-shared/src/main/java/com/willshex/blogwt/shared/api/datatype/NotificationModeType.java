// 
//  NotificationModeType.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum NotificationModeType {
	NotificationModeTypeSms("sms"),
	NotificationModeTypeEmail("email"),
	NotificationModeTypePush("push"),;
	private String value;
	private static Map<String, NotificationModeType> valueLookup = null;

	public String toString () {
		return value;
	}

	private NotificationModeType (String value) {
		this.value = value;
	}

	public static NotificationModeType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, NotificationModeType>();
			for (NotificationModeType currentNotificationModeType : NotificationModeType
					.values()) {
				valueLookup.put(currentNotificationModeType.value,
						currentNotificationModeType);
			}
		}
		return valueLookup.get(value);
	}
}
//
//  MetaNotificationHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MetaNotificationHelper {

	/**
	 * @return
	 */
	public static List<MetaNotification> createAll () {
		return new ArrayList<>();
	}

	public static MetaNotification createFull (String code, String name,
			String group, String content, List<NotificationModeType> defaults) {
		return new MetaNotification().code(code).content(content).group(group)
				.name(name).modes(allModes()).defaults(defaults);
	}

	public static List<NotificationModeType> allModes () {
		List<NotificationModeType> allModes = new ArrayList<>();
		for (NotificationModeType type : NotificationModeType.values()) {
			allModes.add(type);
		}
		return allModes;
	}

}

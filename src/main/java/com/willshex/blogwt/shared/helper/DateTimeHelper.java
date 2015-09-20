//
//  DateTimeHelper.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 15 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.Date;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DateTimeHelper {
	public static String ago (Date date) {
		return date == null ? "not yet" : ago(date, true);
	}

	public static String ago (Date date, boolean blur) {
		String ago;

		long time = date.getTime();
		long timeNow = (new Date()).getTime();

		double seconds = Math.floor((double) (timeNow - time) / 1000.0);

		double interval;

		if ((interval = Math.floor(seconds / 31536000.0)) > 1) {
			ago = (int) interval + " years ago";
		} else if ((interval = Math.floor(seconds / 2592000.0)) > 1) {
			ago = (int) interval + " months ago";
		} else if ((interval = Math.floor(seconds / 86400.0)) > 1) {
			if (blur) {
				if (interval < 2) {
					ago = "yesterday";
				} else if (interval > 7 && interval < 14) {
					ago = "last week";
				} else {
					ago = (int) interval + " days ago";
				}
			} else {
				ago = (int) interval + " days ago";
			}
		} else if ((interval = Math.floor(seconds / 3600.0)) > 1) {
			if (blur) {
				if (interval > 12) {
					ago = "earlier today";
				} else {
					ago = (int) interval + " hours ago";
				}
			} else {
				ago = (int) interval + " hours ago";
			}
		} else if ((interval = Math.floor(seconds / 60.0)) > 1) {
			ago = (int) interval + " minutes ago";
		} else {
			if (blur) {
				ago = "less than a minute ago";
			} else {
				ago = (int) Math.floor(seconds) + " seconds ago";
			}
		}

		return ago;
	}
}

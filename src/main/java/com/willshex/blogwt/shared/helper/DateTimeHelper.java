//
//  DateTimeHelper.java
//  blogwt
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
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String CARD_DATE_FORMAT = "MM/yy";

	public static final long MILLIS_PER_SEC = 1000L;
	public static final long MILLIS_PER_MIN = 60L * MILLIS_PER_SEC;
	public static final long MILLIS_PER_HOUR = 60L * MILLIS_PER_MIN;
	public static final long MILLIS_PER_DAY = 24L * MILLIS_PER_HOUR;
	public static final long YEARS_100 = 100L * 365L * MILLIS_PER_DAY;
	public static final long YEARS_2000 = 2000L * 365L * MILLIS_PER_DAY;

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

	public static Date now () {
		return new Date();
	}

	public static String forDays (Date date) {
		long diff = date.getTime() - now().getTime();
		return diff > YEARS_100 ? "indefintly"
				: ("for " + (diff / MILLIS_PER_DAY) + " days");
	}

	/**
	 * @param minutes
	 * @return
	 */
	public static Date minutesFromNow (int minutes) {
		return minutesFrom(minutes, now());
	}

	public static Date minutesFrom (int minutes, Date from) {
		return new Date(from.getTime() + ((long) minutes * MILLIS_PER_MIN));
	}

	public static boolean isLessThanMinutesAway (Date date, int minutes) {
		return date.getTime() < minutesFromNow(minutes).getTime();
	}
}

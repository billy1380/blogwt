//
//  GoogleAnalyticsHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 29 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.core.client.GWT;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GoogleAnalyticsHelper {

	public static void sendPageView (String current) {
		GWT.log("Sending current stack [" + current + "] to GA.");

		if (jsIsAvailable()) {
			jsSendPageView(current);
		}
	}

	private static native void jsSendPageView (String slug) /*-{
	$wnd.ga('send', 'pageview', slug);
	}-*/;

	private static native boolean jsIsAvailable () /*-{
	return $wnd.ga != undefined;
	}-*/;

}

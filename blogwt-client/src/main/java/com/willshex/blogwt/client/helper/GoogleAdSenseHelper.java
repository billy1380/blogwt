//
//  GoogleAdSenseHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Jun 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GoogleAdSenseHelper {

	private static native void trigger () /*-{
	(adsbygoogle = $wnd.adsbygoogle || []).push({});
	}-*/;

	public static void addTo (Element element, String adSenseKey, String slot) {
		element.setInnerHTML(
				"<ins class=\"adsbygoogle\" style=\"display:block\""
						+ "data-ad-client=\"" + adSenseKey
						+ "\" data-ad-slot=\"" + slot + "\""
						+ "data-ad-format=\"auto\" ></ins>");

		Scheduler.get().scheduleDeferred(GoogleAdSenseHelper::trigger);
	}

	public static void connectAds (String slot, Element... elements) {
		String adSenseKey = PropertyController.get()
				.stringProperty(PropertyHelper.GOOGLE_AD_SENSE_KEY);

		if (adSenseKey == null || adSenseKey.trim().isEmpty()) {
			for (Element element : elements) {
				element.getStyle().setDisplay(Display.NONE);
			}
		} else {
			for (Element element : elements) {
				element.getStyle().clearDisplay();
				addTo(element, adSenseKey, slot);
			}
		}
	}

}

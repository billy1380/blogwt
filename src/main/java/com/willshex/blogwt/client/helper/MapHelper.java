//
//  MapHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 21 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MapHelper {

	/**
	 * @param element
	 * @param lines
	 * @param params
	 */
	public static void showMap (Element element, List<String> lines,
			Map<String, String> params) {
		Style s = element.getStyle();

		// TODO: get these values from the parameters or lines
		s.setWidth(100.0, Unit.PCT);
		s.setHeight(200.0, Unit.PX);

		showMap(element, -34.397f, 150.644f, 8);
	}

	private static native void showMap (Element element, float lat, float lng,
			float zoom) /*-{
						var mapOptions = {
						zoom: zoom,
						center: {lat: lat, lng: lng}
						};
						var map = new google.maps.Map(element, mapOptions);
						}-*/;

}

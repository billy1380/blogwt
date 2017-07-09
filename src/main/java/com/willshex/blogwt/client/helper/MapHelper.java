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

		float width = floatParam(params, "width", 100);
		Unit widthUnits = unitParam(params, "widthUnits", Unit.PCT);
		s.setWidth(width, widthUnits);

		float height = floatParam(params, "height", 200);
		Unit heightUnits = unitParam(params, "heightUnits", Unit.PT);
		s.setHeight(height, heightUnits);

		float lat = floatParam(params, "lat", -34.397f);
		float lng = floatParam(params, "lng", 150.644f);

		int zoom = intParam(params, "zoom", 8);

		String markerName = params.get("markerName");

		showMap(element, lat, lng, zoom, markerName);
	}

	private static int intParam (Map<String, String> params, String name,
			int defaultValue) {
		String paramValue = params.get(name);
		int value = defaultValue;
		if (paramValue != null) {
			try {
				value = Integer.valueOf(paramValue).intValue();
			} catch (Exception ex) {}
		}

		return value;
	}

	public static float floatParam (Map<String, String> params, String name,
			float defaultValue) {
		String paramValue = params.get(name);
		float value = defaultValue;
		if (paramValue != null) {
			try {
				value = Float.valueOf(paramValue).floatValue();
			} catch (Exception ex) {}
		}

		return value;
	}

	public static Unit unitParam (Map<String, String> params, String name,
			Unit defaultValue) {
		String paramUnit = params.get(name);
		Unit units = defaultValue;
		if (paramUnit != null) {
			try {
				units = Unit.valueOf(paramUnit);
			} catch (Exception ex) {}
		}

		return units;
	}

	private static native void showMap (Element element, float lat, float lng,
			int zoom, String markerName) /*-{
	var myLatlng = new google.maps.LatLng(lat, lng);
	var mapOptions = {
	    zoom : zoom,
	    center : myLatlng
	};
	var map = new google.maps.Map(element, mapOptions);
	if (markerName !== undefined) {
	    var marker = new google.maps.Marker({
		position : myLatlng,
		map : map,
		title : (markerName === "<nameless>" ? undefined : markerName)
	    });
	}
	}-*/;

}

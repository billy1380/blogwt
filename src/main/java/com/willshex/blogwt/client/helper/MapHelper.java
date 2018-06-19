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

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.helper.MapHelper.MapsReadyEventHandler.MapsReadyEvent;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MapHelper {

	public static final String WIDTH_PARAM_NAME = "width";
	public static final String WIDTH_UNITS_PARAM_NAME = "widthUnits";
	public static final String HEIGHT_PARAM_NAME = "height";
	public static final String HEIGHT_UNITS_PARAM_NAME = "heightUnits";
	public static final String LAT_PARAM_NAME = "lat";
	public static final String LNG_PARAM_NAME = "lng";
	public static final String ZOOM_PARAM_NAME = "zoom";
	public static final String MARKER_NAME_PARAM_NAME = "markerName";

	@FunctionalInterface
	public interface MapsReadyEventHandler extends EventHandler {
		public static final GwtEvent.Type<MapsReadyEventHandler> TYPE = new GwtEvent.Type<MapsReadyEventHandler>();

		public void mapsReady ();

		public class MapsReadyEvent extends GwtEvent<MapsReadyEventHandler> {

			/* (non-Javadoc)
			 * 
			 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType() */
			@Override
			public com.google.gwt.event.shared.GwtEvent.Type<MapsReadyEventHandler> getAssociatedType () {
				return TYPE;
			}

			/* (non-Javadoc)
			 * 
			 * @see
			 * com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.
			 * event. shared.EventHandler) */
			@Override
			protected void dispatch (MapsReadyEventHandler handler) {
				handler.mapsReady();
			}
		}
	}

	private static boolean added = false;
	private static boolean initialised = false;
	private static Object geocoder;

	public static Object showMap (Element element, List<String> lines,
			Map<String, String> params) {
		Style s = element.getStyle();

		float width = floatParam(params, WIDTH_PARAM_NAME, 100);
		Unit widthUnits = unitParam(params, WIDTH_UNITS_PARAM_NAME, Unit.PCT);
		s.setWidth(width, widthUnits);

		float height = floatParam(params, HEIGHT_PARAM_NAME, 200);
		Unit heightUnits = unitParam(params, HEIGHT_UNITS_PARAM_NAME, Unit.PT);
		s.setHeight(height, heightUnits);

		float lat = floatParam(params, LAT_PARAM_NAME, -34.397f);
		float lng = floatParam(params, LNG_PARAM_NAME, 150.644f);

		int zoom = intParam(params, ZOOM_PARAM_NAME, 8);

		String markerName = params.get(MARKER_NAME_PARAM_NAME);

		return showMap(element, lat, lng, zoom, markerName);
	}

	private static native void registerMapsInitialisedMethod () /*-{
	$wnd.mapsInitialised = $entry(@com.willshex.blogwt.client.helper.MapHelper::mapsInitialised());
	}-*/;

	public static void inject () {
		if (!added) {
			added = true;

			registerMapsInitialisedMethod();

			ScriptInjector.fromUrl(
					"https://maps.googleapis.com/maps/api/js?callback=mapsInitialised&key="
							+ PropertyController.get().stringProperty(
									PropertyHelper.MARKDOWN_MAPS_API_KEY))
					.setWindow(ScriptInjector.TOP_WINDOW).inject();
		}

		if (initialised) {
			DefaultEventBus.get().fireEvent(new MapsReadyEvent());
		}
	}

	private static final void mapsInitialised () {
		initialised = true;

		geocoder = createGeocoder();
		DefaultEventBus.get().fireEvent(new MapsReadyEvent());
	}

	private static native Object createGeocoder () /*-{
	return new $wnd.google.maps.Geocoder();
	}-*/;

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

	private static float floatParam (Map<String, String> params, String name,
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

	private static Unit unitParam (Map<String, String> params, String name,
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

	public static void markAddress (String address, Object map) {
		markAddress(address, map, geocoder);
	}

	private static native void markAddress (String address, Object map,
			Object geocoder) /*-{
	geocoder.geocode({
	    'address' : address
	}, function(results, status) {
	    if (status === 'OK') {
		map.setCenter(results[0].geometry.location);
		var marker = new $wnd.google.maps.Marker({
		    map : map,
		    position : results[0].geometry.location
		});
	    } else {
		Console.error('Geocode was not successful for the following reason: ' + status);
	    }
	});
	}-*/;

	private static native Object showMap (Element element, float lat, float lng,
			int zoom, String markerName) /*-{
	var myLatlng = {
	    lat : lat,
	    lng : lng
	};

	var mapOptions = {
	    zoom : zoom,
	    center : myLatlng
	};

	var map = new $wnd.google.maps.Map(element, mapOptions);

	if (markerName !== undefined) {
	    var marker = new $wnd.google.maps.Marker({
		position : myLatlng,
		map : map,
		title : (markerName === "<nameless>" ? undefined : markerName)
	    });
	}

	return map;
	}-*/;

}

//
//  PropertyController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertyController {
	private static PropertyController one = null;

	/**
	 * @return
	 */
	public static PropertyController get () {
		if (one == null) {
			one = new PropertyController();
		}

		return one;
	}

	private Map<String, Property> propertyLookup = new HashMap<String, Property>();
	private List<Property> properties = null;

	private PropertyController () {
		String propertiesJson = properties();

		if (propertiesJson != null) {
			JsonArray jsonPropertyArray = (new JsonParser()).parse(
					propertiesJson).getAsJsonArray();
			Property item = null;
			for (int i = 0; i < jsonPropertyArray.size(); i++) {
				if (properties == null) {
					properties = new ArrayList<Property>();
				} else {
					properties.clear();
				}

				if (jsonPropertyArray.get(i).isJsonObject()) {
					(item = new Property()).fromJson(jsonPropertyArray.get(i)
							.getAsJsonObject());
					propertyLookup.put(item.name, item);
				}
			}
		}
	}

	public List<Property> blog () {
		return properties;
	}

	/**
	 * @return
	 */
	public SafeUri copyrightHolderUrl () {
		Property p = propertyLookup.get(PropertyHelper.COPYRIGHT_URL);
		return UriUtils
				.fromSafeConstant(PropertyHelper.empty(p) ? "https://www.willshex.com"
						: p.value);
	}

	/**
	 * @return
	 */
	public String copyrightHolder () {
		Property p = propertyLookup.get(PropertyHelper.COPYRIGHT_HOLDER);
		return PropertyHelper.empty(p) ? "WillShex Limited" : p.value;
	}

	/**
	 * @return
	 */
	public String title () {
		Property p = propertyLookup.get(PropertyHelper.TITLE);
		return PropertyHelper.empty(p) ? "Blogwt" : p.value;
	}

	/**
	 * @return
	 */
	public String extendedTitle () {
		Property p = propertyLookup.get(PropertyHelper.EXTENDED_TITLE);
		return PropertyHelper.empty(p) ? "a blog is a blog" : p.value;
	}

	/**
	 * @return
	 */
	public Date started () {
		Property p = propertyLookup.get(PropertyHelper.TITLE);
		return PropertyHelper.empty(p) ? new Date(1199188800000L) : p.created;
	}

	private static native String properties ()
	/*-{
		return $wnd['properties'];
	}-*/;
}

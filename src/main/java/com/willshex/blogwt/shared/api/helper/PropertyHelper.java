//
//  PropertyHelper.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.willshex.blogwt.shared.api.datatype.Property;

/**
 * @author billy1380
 *
 */
public class PropertyHelper {
	public static final String TITLE = "setup.title";
	public static final String EXTENDED_TITLE = "setup.extended.title";
	public static final String COPYRIGHT_HOLDER = "setup.copyright.holder";
	public static final String COPYRIGHT_URL = "setup.copyright.url";

	public static final String TITLE_DESCRIPTION = "Blog Title";
	public static final String EXTENDED_TITLE_DESCRIPTION = "Extended Title (Short Description)";
	public static final String COPYRIGHT_HOLDER_DESCRIPTION = "Copyright Holder Name";
	public static final String COPYRIGHT_URL_DESCRIPTION = "Copyright Url";

	public static Map<String, Property> toLookup (List<Property> properties) {
		Map<String, Property> lookup = new HashMap<String, Property>();
		for (Property property : properties) {
			lookup.put(property.name, property);
		}
		return lookup;
	}

	public static Property createTitle (String value) {
		return new Property().name(TITLE).description(TITLE_DESCRIPTION)
				.value(value);
	}

	public static Property createExtendedTitle (String value) {
		return new Property().name(EXTENDED_TITLE)
				.description(EXTENDED_TITLE_DESCRIPTION).value(value);
	}

	public static Property createCopyrightHolder (String value) {
		return new Property().name(COPYRIGHT_HOLDER)
				.description(COPYRIGHT_HOLDER_DESCRIPTION).value(value);
	}

	public static Property createCopyrightUrl (String value) {
		return new Property().name(COPYRIGHT_URL)
				.description(COPYRIGHT_URL_DESCRIPTION).value(value);
	}
}

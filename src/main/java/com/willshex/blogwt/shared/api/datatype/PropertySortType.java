//  
//  PropertySortType.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 13, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum PropertySortType {
	PropertySortTypeName("name"),
	PropertySortTypeGroup("group"),
	PropertySortTypeValue("value"),
	PropertySortTypeType("type"), ;
	private String value;
	private static Map<String, PropertySortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private PropertySortType (String value) {
		this.value = value;
	}

	public static PropertySortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PropertySortType>();
			for (PropertySortType currentPropertySortType : PropertySortType
					.values()) {
				valueLookup.put(currentPropertySortType.value,
						currentPropertySortType);
			}
		}
		return valueLookup.get(value);
	}
}
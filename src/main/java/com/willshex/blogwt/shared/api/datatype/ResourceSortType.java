//  
//  ResourceSortType.java
//  blogwt
//
//  Created by William Shakour on August 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum ResourceSortType {
	ResourceSortTypeId("id"), ;
	private String value;
	private static Map<String, ResourceSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private ResourceSortType (String value) {
		this.value = value;
	}

	public static ResourceSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, ResourceSortType>();
			for (ResourceSortType currentResourceSortType : ResourceSortType
					.values()) {
				valueLookup.put(currentResourceSortType.value,
						currentResourceSortType);
			}
		}
		return valueLookup.get(value);
	}
}
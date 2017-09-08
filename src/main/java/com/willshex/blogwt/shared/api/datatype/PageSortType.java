//  
//  PageSortType.java
//  blogwt
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum PageSortType {
	PageSortTypeId("id"),
	PageSortTypeCreated("created"),
	PageSortTypeSlug("slug"),
	PageSortTypeParent("parent"),
	PageSortTypePriority("priority"),;
	private String value;
	private static Map<String, PageSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private PageSortType (String value) {
		this.value = value;
	}

	public static PageSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PageSortType>();
			for (PageSortType currentPageSortType : PageSortType.values()) {
				valueLookup.put(currentPageSortType.value, currentPageSortType);
			}
		}
		return valueLookup.get(value);
	}
}
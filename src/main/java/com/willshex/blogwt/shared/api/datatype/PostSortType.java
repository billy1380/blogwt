//  
//  PostSortType.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum PostSortType {
	PostSortTypeAuthor("author"),
	PostSortTypePublished("published"),
	PostSortTypeSlug("slug"),
	PostSortTypeVisible("visible"), ;
	private String value;
	private static Map<String, PostSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private PostSortType (String value) {
		this.value = value;
	}

	public static PostSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PostSortType>();
			for (PostSortType currentPostSortType : PostSortType.values()) {
				valueLookup.put(currentPostSortType.value, currentPostSortType);
			}
		}
		return valueLookup.get(value);
	}
}
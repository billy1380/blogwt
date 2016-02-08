//  
//  RelationshipSortType.java
//  blogwt
//
//  Created by William Shakour on February 8, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum RelationshipSortType {
	RelationshipSortTypeType("type"),
	RelationshipSortTypeOne("one"),
	RelationshipSortTypeAnother("another"),;
	private String value;
	private static Map<String, RelationshipSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private RelationshipSortType (String value) {
		this.value = value;
	}

	public static RelationshipSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, RelationshipSortType>();
			for (RelationshipSortType currentRelationshipSortType : RelationshipSortType
					.values()) {
				valueLookup.put(currentRelationshipSortType.value,
						currentRelationshipSortType);
			}
		}
		return valueLookup.get(value);
	}
}
//  
//  PermissionSortType.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum PermissionSortType {
	PermissionSortTypeId("id"),
	PermissionSortTypeCode("code"), ;
	private String value;
	private static Map<String, PermissionSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private PermissionSortType (String value) {
		this.value = value;
	}

	public static PermissionSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PermissionSortType>();
			for (PermissionSortType currentPermissionSortType : PermissionSortType
					.values()) {
				valueLookup.put(currentPermissionSortType.value,
						currentPermissionSortType);
			}
		}
		return valueLookup.get(value);
	}
}
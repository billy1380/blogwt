//  
//  RoleSortType.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum RoleSortType {
	RoleSortTypeId("id"),
	RoleSortTypeCode("code"), ;
	private String value;
	private static Map<String, RoleSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private RoleSortType (String value) {
		this.value = value;
	}

	public static RoleSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, RoleSortType>();
			for (RoleSortType currentRoleSortType : RoleSortType.values()) {
				valueLookup.put(currentRoleSortType.value, currentRoleSortType);
			}
		}
		return valueLookup.get(value);
	}
}
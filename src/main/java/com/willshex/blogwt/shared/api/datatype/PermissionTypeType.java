//  
//  PermissionTypeType.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum PermissionTypeType {
	PermissionTypeTypeUser("user"), PermissionTypeTypeSpecial("special"), ;
	private String value;
	private static Map<String, PermissionTypeType> valueLookup = null;

	public String toString() {
		return value;
	}

	private PermissionTypeType(String value) {
		this.value = value;
	}

	public static PermissionTypeType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PermissionTypeType>();
			for (PermissionTypeType currentPermissionTypeType : PermissionTypeType.values()) {
				valueLookup.put(currentPermissionTypeType.value, currentPermissionTypeType);
			}
		}
		return valueLookup.get(value);
	}
}
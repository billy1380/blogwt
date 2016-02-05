//  
//  FieldTypeType.java
//  blogwt
//
//  Created by William Shakour on November 18, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum FieldTypeType {
	FieldTypeTypeText("text"),
	FieldTypeTypeLongText("longText"),
	FieldTypeTypeSingleOption("singleOption"),
	FieldTypeTypeCaptcha("captcha"), ;
	private String value;
	private static Map<String, FieldTypeType> valueLookup = null;

	public String toString () {
		return value;
	}

	private FieldTypeType (String value) {
		this.value = value;
	}

	public static FieldTypeType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, FieldTypeType>();
			for (FieldTypeType currentFieldTypeType : FieldTypeType.values()) {
				valueLookup.put(currentFieldTypeType.value,
						currentFieldTypeType);
			}
		}
		return valueLookup.get(value);
	}
}
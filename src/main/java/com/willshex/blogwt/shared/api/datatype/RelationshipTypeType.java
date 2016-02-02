//  
//  RelationshipTypeType.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 2, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum RelationshipTypeType {
	RelationshipTypeTypeFollow("follow"),
	RelationshipTypeTypeBlock("block"),;
	private String value;
	private static Map<String, RelationshipTypeType> valueLookup = null;

	public String toString () {
		return value;
	}

	private RelationshipTypeType (String value) {
		this.value = value;
	}

	public static RelationshipTypeType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, RelationshipTypeType>();
			for (RelationshipTypeType currentRelationshipTypeType : RelationshipTypeType
					.values()) {
				valueLookup.put(currentRelationshipTypeType.value,
						currentRelationshipTypeType);
			}
		}
		return valueLookup.get(value);
	}
}
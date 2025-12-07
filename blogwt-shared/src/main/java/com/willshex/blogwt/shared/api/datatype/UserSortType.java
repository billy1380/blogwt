//  
//  UserSortType.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum UserSortType {
	UserSortTypeId("id"),
	UserSortTypeUsername("username"),
	UserSortTypeEmail("email"),
	UserSortTypeForename("forename"),
	UserSortTypeSurname("surname"),
	UserSortTypeActionCode("actionCode"),
	UserSortTypeAdded("added"), ;
	private String value;
	private static Map<String, UserSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private UserSortType (String value) {
		this.value = value;
	}

	public static UserSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, UserSortType>();
			for (UserSortType currentUserSortType : UserSortType.values()) {
				valueLookup.put(currentUserSortType.value, currentUserSortType);
			}
		}
		return valueLookup.get(value);
	}
}
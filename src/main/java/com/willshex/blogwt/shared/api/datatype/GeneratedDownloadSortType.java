// 
//  GeneratedDownloadSortType.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum GeneratedDownloadSortType {
	GeneratedDownloadSortTypeId("id"),
	GeneratedDownloadSortTypeCreated("created"),
	GeneratedDownloadSortTypeUser("user"),;
	private String value;
	private static Map<String, GeneratedDownloadSortType> valueLookup = null;

	public String toString () {
		return value;
	}

	private GeneratedDownloadSortType (String value) {
		this.value = value;
	}

	public static GeneratedDownloadSortType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, GeneratedDownloadSortType>();
			for (GeneratedDownloadSortType currentGeneratedDownloadSortType : GeneratedDownloadSortType
					.values()) {
				valueLookup.put(currentGeneratedDownloadSortType.value,
						currentGeneratedDownloadSortType);
			}
		}
		return valueLookup.get(value);
	}
}
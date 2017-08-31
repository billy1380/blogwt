// 
//  GeneratedDownloadStatusType.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum GeneratedDownloadStatusType {
	GeneratedDownloadStatusTypeGenerating("generating"),
	GeneratedDownloadStatusTypeError("error"),
	GeneratedDownloadStatusTypeReady("ready"),;
	private String value;
	private static Map<String, GeneratedDownloadStatusType> valueLookup = null;

	public String toString () {
		return value;
	}

	private GeneratedDownloadStatusType (String value) {
		this.value = value;
	}

	public static GeneratedDownloadStatusType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, GeneratedDownloadStatusType>();
			for (GeneratedDownloadStatusType currentGeneratedDownloadStatusType : GeneratedDownloadStatusType
					.values()) {
				valueLookup.put(currentGeneratedDownloadStatusType.value,
						currentGeneratedDownloadStatusType);
			}
		}
		return valueLookup.get(value);
	}
}
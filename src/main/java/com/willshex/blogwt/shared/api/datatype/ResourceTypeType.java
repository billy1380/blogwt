//  
//  ResourceTypeType.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.HashMap;
import java.util.Map;

public enum ResourceTypeType {
	ResourceTypeTypeBase64Image("base64Image"), ResourceTypeTypeGoogleCloudServiceImage("googleCloudServiceImage"), ResourceTypeTypeBlobStoreImage(
			"blobStoreImage"), ResourceTypeTypeUrlImage("urlImage"), ResourceTypeTypeYoutubeVideo("youtubeVideo"), ResourceTypeTypeUrlVideo("urlVideo"), ;
	private String value;
	private static Map<String, ResourceTypeType> valueLookup = null;

	public String toString() {
		return value;
	}

	private ResourceTypeType(String value) {
		this.value = value;
	}

	public static ResourceTypeType fromString(String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, ResourceTypeType>();
			for (ResourceTypeType currentResourceTypeType : ResourceTypeType.values()) {
				valueLookup.put(currentResourceTypeType.value, currentResourceTypeType);
			}
		}
		return valueLookup.get(value);
	}
}
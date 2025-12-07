// 
//  PayloadPrioriyType.java
//  blogwt
// 
//  Created by William Shakour on June 16, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.helper.push;

import java.util.HashMap;
import java.util.Map;

public enum PayloadPrioriyType {
	PayloadPrioriyTypeNormal("normal"),
	PayloadPrioriyTypeHigh("high"),;
	private String value;
	private static Map<String, PayloadPrioriyType> valueLookup = null;

	public String toString () {
		return value;
	}

	private PayloadPrioriyType (String value) {
		this.value = value;
	}

	public static PayloadPrioriyType fromString (String value) {
		if (valueLookup == null) {
			valueLookup = new HashMap<String, PayloadPrioriyType>();
			for (PayloadPrioriyType currentPayloadPrioriyType : PayloadPrioriyType
					.values()) {
				valueLookup.put(currentPayloadPrioriyType.value,
						currentPayloadPrioriyType);
			}
		}
		return valueLookup.get(value);
	}
}
//
//  PropertyController.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.List;

import com.willshex.blogwt.shared.api.datatype.Property;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertyController {
	private static PropertyController one = null;

	/**
	 * @return
	 */
	public static PropertyController get () {
		if (one == null) {
			one = new PropertyController();
		}

		return one;
	}

	public List<Property> blog () {
		return null;
	}
}

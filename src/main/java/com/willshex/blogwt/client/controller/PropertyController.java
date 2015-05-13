//
//  PropertyController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Date;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
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

	/**
	 * @return
	 */
	public SafeUri copyrightHolderUrl () {
		return UriUtils.fromSafeConstant("https://www.willshex.com");
	}

	/**
	 * @return
	 */
	public String copyrightHolder () {
		return "WillShex Limited";
	}

	/**
	 * @return
	 */
	public String name () {
		return "Blogwt";
	}

	/**
	 * @return
	 */
	public Date started () {
		return new Date(1199188800000L);
	}
}

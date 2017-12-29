//
//  AddressHelper.java
//  bidly
//
//  Created by William Shakour (billy1380) on 29 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper.bidly;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddressHelper {

	public static final String postcodeArea (String postcode) {
		postcode = postcode.replace(" ", "");
		postcode = postcode.substring(0, postcode.length() - 3);
		return postcode;
	}
}

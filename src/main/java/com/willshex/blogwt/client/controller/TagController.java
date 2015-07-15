//
//  TagController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TagController {
	private static TagController one = null;

	public static TagController get () {
		if (one == null) {
			one = new TagController();
		}

		return one;
	}

}

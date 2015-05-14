//
//  RoleHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.helper;

import com.willshex.blogwt.shared.api.datatype.Role;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleHelper {
	public static final String ADMIN = "ADM";

	public static Role createAdmin () {
		return new Role().code(ADMIN);
	}
}

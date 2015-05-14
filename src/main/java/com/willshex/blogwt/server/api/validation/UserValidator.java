//
//  UserValidator.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.gson.json.service.server.InputValidationException;

/**
 * @author billy1380
 *
 */
public class UserValidator extends ApiValidator {

	public static User validate (User user, String name)
			throws InputValidationException {
		return user;
	}

	public static <T extends Iterable<User>> T validateAll (T users, String name)
			throws InputValidationException {
		return users;
	}

}

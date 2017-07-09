//
//  UserHelper.java
//  blogwt
//
//  Created by billy1380 on 8 Jul 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author billy1380
 *
 */
public class UserHelper extends com.willshex.blogwt.shared.helper.UserHelper {

	public static SafeUri avatar (User user) {
		return user.avatar == null ? Resources.RES.newUser().getSafeUri()
				: UriUtils.fromString(user.avatar);
	}

}

//
//  ActionHandler.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jul 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api;

import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.helper.UserHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class ActionHandler<I extends Request, O extends Response>
		extends com.willshex.gson.web.service.server.ActionHandler<I, O> {

	@Override
	public void clearSensitiveFields (O output) {
		super.clearSensitiveFields(output);

		UserHelper.stripPassword(
				output.session == null ? null : output.session.user);
	}

}

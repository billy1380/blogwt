// 
//  PushTokenService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.pushtoken;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.shared.api.datatype.PushToken;

final class PushTokenService implements IPushTokenService {
	public String getName () {
		return NAME;
	}

	@Override
	public PushToken getPushToken (Long id) {
		return load().id(id.longValue()).now();
	}

	/**
	 * @return
	 */
	private LoadType<PushToken> load () {
		return provide().load().type(PushToken.class);
	}

	@Override
	public PushToken addPushToken (PushToken pushToken) {
		if (pushToken.created == null) {
			pushToken.created = new Date();
		}

		if (pushToken.user != null) {
			pushToken.userKey = Key.create(pushToken.user);
		}

		Key<PushToken> key = provide().save().entity(pushToken).now();
		pushToken.id = Long.valueOf(key.getId());

		return pushToken;
	}

	@Override
	public PushToken updatePushToken (PushToken pushToken) {
		provide().save().entity(pushToken).now();

		return pushToken;
	}

	@Override
	public void deletePushToken (PushToken pushToken) {
		throw new UnsupportedOperationException();
	}

}
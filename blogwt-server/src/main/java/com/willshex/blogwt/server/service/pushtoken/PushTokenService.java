// 
//  PushTokenService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.pushtoken;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.PushTokenSortType;
import com.willshex.blogwt.shared.api.datatype.User;

final class PushTokenService implements IPushTokenService {
	public String getName() {
		return NAME;
	}

	@Override
	public PushToken getPushToken(Long id) {
		return id(load(), id);
	}

	/**
	 * @return
	 */
	private LoadType<PushToken> load() {
		return provide().load().type(PushToken.class);
	}

	@Override
	public PushToken addPushToken(PushToken pushToken) {
		if (pushToken.created == null) {
			pushToken.created = new Date();
		}

		if (pushToken.user != null) {
			pushToken.userKey = ObjectifyService.key(pushToken.user);
		}

		Key<PushToken> key = provide().save().entity(pushToken).now();
		pushToken.id = Long.valueOf(key.getId());

		return pushToken;
	}

	@Override
	public PushToken updatePushToken(PushToken pushToken) {
		provide().save().entity(pushToken).now();

		return pushToken;
	}

	@Override
	public void deletePushToken(PushToken pushToken) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.pushtoken.IPushTokenService#
	 * getUserPlatformPushToken(com.willshex.blogwt.shared.api.datatype.User,
	 * java.lang.String)
	 */
	@Override
	public PushToken getUserPlatformPushToken(User user, String platform) {
		return PersistenceHelper.one(load()
				.filter(map(PushTokenSortType.PushTokenSortTypeUser), user)
				.filter(map(PushTokenSortType.PushTokenSortTypePlatform),
						platform));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.pushtoken.IPushTokenService#
	 * getUserPushTokens(com.willshex.blogwt.shared.api.datatype.User)
	 */
	@Override
	public List<PushToken> getUserPushTokens(User user) {
		return load().filter(map(PushTokenSortType.PushTokenSortTypeUser), user)
				.list();
	}

}

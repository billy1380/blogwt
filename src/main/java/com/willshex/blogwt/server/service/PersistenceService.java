//
//  PeristenceService.java
//  blogwt
//
//  Created by William Shakour on 10 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.User;

/**
 * @author billy1380
 * 
 */
public class PersistenceService {
	static {
		factory().register(User.class);
		factory().register(Session.class);
		factory().register(Post.class);
		factory().register(Resource.class);
		factory().register(Permission.class);
		factory().register(Role.class);
	}

	public static Objectify ofy () {
		return ObjectifyService.ofy();
	}

	private static ObjectifyFactory factory () {
		return ObjectifyService.factory();
	}

	public static <T> Collection<Long> keysToIds (Collection<Key<T>> keys) {
		List<Long> collection = new ArrayList<Long>();
		for (Key<T> key : keys) {
			collection.add(Long.valueOf(key.getId()));
		}
		return collection;
	}
}
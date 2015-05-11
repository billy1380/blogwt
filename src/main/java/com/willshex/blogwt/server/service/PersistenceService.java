//
//  PeristenceService.java
//  blogwt
//
//  Created by William Shakour on 11 Jul 2013.
//  Copyright © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.server.service;

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

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	private static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}
}
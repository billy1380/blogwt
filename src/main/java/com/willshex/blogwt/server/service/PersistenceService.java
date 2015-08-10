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
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
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
		factory().register(Property.class);
		factory().register(PostContent.class);
		factory().register(Page.class);
		factory().register(Tag.class);
	}

	public static Objectify ofy () {
		return ObjectifyService.ofy();
	}

	private static ObjectifyFactory factory () {
		return ObjectifyService.factory();
	}

	public static <T> Collection<Long> keysToIds (Collection<Key<T>> keys) {
		List<Long> collection = null;
		if (keys != null) {
			collection = new ArrayList<Long>();
			for (Key<T> key : keys) {
				collection.add(Long.valueOf(key.getId()));
			}
		}
		return collection;
	}

	public static <T extends DataType> T dataType (Class<T> t, Key<T> key) {
		T i = null;
		if (key != null) {
			try {
				i = t.newInstance();
				i.id(Long.valueOf(key.getId()));
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return i;
	}

	/**
	 * @param t
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DataType> List<T> dataTypeList (Class<T> t,
			Collection<Key<T>> keys) {
		List<T> list = new ArrayList<T>();

		try {
			for (Key<T> key : keys) {
				list.add((T) t.newInstance().id(Long.valueOf(key.getId())));
			}
		} catch (InstantiationException | IllegalAccessException ex) {}

		return list;
	}
}
//
//  PeristenceService.java
//  blogwt
//
//  Created by William Shakour on 10 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.Translators;
import com.willshex.blogwt.server.service.persistence.translator.PermissionTypeTypeTranslatorFactory;
import com.willshex.blogwt.server.service.persistence.translator.RelationshipTypeTypeTranslatorFactory;
import com.willshex.blogwt.server.service.persistence.translator.ResourceTypeTypeTranslatorFactory;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Relationship;
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
		Translators translators = factory().getTranslators();
		translators.add(new PermissionTypeTypeTranslatorFactory());
		translators.add(new RelationshipTypeTypeTranslatorFactory());
		translators.add(new ResourceTypeTypeTranslatorFactory());
		
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
		factory().register(ArchiveEntry.class);
		factory().register(Notification.class);
		factory().register(MetaNotification.class);
		factory().register(Relationship.class);
	}

	public static Objectify ofy () {
		return ObjectifyService.ofy();
	}

	private static ObjectifyFactory factory () {
		return ObjectifyService.factory();
	}

	public static <T> List<Long> keysToIds (Collection<Key<T>> keys) {
		List<Long> collection = null;
		if (keys != null) {
			collection = new ArrayList<Long>();
			for (Key<T> key : keys) {
				collection.add(Long.valueOf(key.getId()));
			}
		}
		return collection;
	}

	public static <T> List<Key<T>> idsToKeys (Class<? extends T> kindClass,
			Collection<Long> ids) {
		List<Key<T>> collection = null;
		if (ids != null) {
			collection = new ArrayList<Key<T>>();
			for (Long id : ids) {
				collection.add(Key.create(kindClass, id.longValue()));
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
			Collection<?> ids) {
		List<T> list = new ArrayList<T>();

		try {
			for (Object id : ids) {
				if (id instanceof Key) {
					list.add((T) t.newInstance()
							.id(Long.valueOf(((Key<T>) id).getId())));
				} else if (id instanceof Long) {
					list.add((T) t.newInstance().id((Long) id));
				}
			}
		} catch (InstantiationException | IllegalAccessException ex) {}

		return list;
	}

	public static <T extends DataType> Map<Long, T> toMap (List<T> items) {
		Map<Long, T> lookup = new HashMap<>();

		for (T item : items) {
			lookup.put(item.id, item);
		}

		return lookup;
	}

	/**
	 * @param dataTypeCollection
	 * @return
	 */
	public static <T extends DataType> List<Long> dataTypeCollectionToIds (
			Collection<T> dataTypeCollection) {
		List<Long> ids = null;

		if (dataTypeCollection != null && dataTypeCollection.size() > 0) {
			ids = new ArrayList<>();

			for (T dataType : dataTypeCollection) {
				ids.add(dataType.id);
			}
		}

		return ids;
	}

}

//
//  PersistenceHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 Apr 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PersistenceHelper {

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

	public static <T> T type (Class<T> t, Key<T> key, Field f) {
		T i = null;
		if (key != null) {
			try {
				i = t.newInstance();
				if (f.getType() == String.class) {
					f.set(i, key.getName());
				} else if (f.getType() == Long.class) {
					f.set(i, Long.valueOf(key.getId()));
				}
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return i;
	}

	public static <T> T type (Class<T> t, Key<T> key) {
		return type(t, key, key(t));
	}

	/**
	 * @param t
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> typeList (Class<T> t, Collection<?> ids) {
		List<T> list = new ArrayList<T>();

		try {
			Field f = key(t);
			T i;
			for (Object id : ids) {
				if (id instanceof Key) {
					list.add(type(t, (Key<T>) id, f));
				} else if (id instanceof Long || id instanceof String) {
					i = (T) t.newInstance();
					f.set(i, id);
					list.add(i);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public static <LS, T> Map<LS, T> typeMap (List<T> items) {
		Map<LS, T> lookup = new HashMap<>();

		if (items.size() > 0) {
			Field f = key(items.get(0).getClass());
			for (T item : items) {
				try {
					lookup.put((LS) f.get(item), item);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return lookup;
	}

	/**
	 * @param dataTypeCollection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <LS, T> List<LS> typeCollectionToIds (
			Collection<T> collection) {
		List<LS> ids = null;

		if (collection != null && collection.size() > 0) {
			ids = new ArrayList<>();
			Field f = key(collection.iterator().next().getClass());

			for (T type : collection) {
				try {
					ids.add((LS) f.get(type));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return ids;
	}

	public static String direction (SortDirectionType sortDirection) {
		String direction = "";
		if (sortDirection != null) {
			switch (sortDirection) {
			case SortDirectionTypeDescending:
				direction = "-";
				break;
			default:
				break;
			}
		}

		return direction;
	}

	/**
	 * Takes a query and adds pagination parameters
	 * @param filter
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortable
	 * @param sortDirection
	 * @return
	 */
	public static <T, E extends Enum<E>> Query<T> pagedAndSorted (
			Query<T> query, Integer start, Integer count, E sortBy,
			ISortable<E> sortable, SortDirectionType sortDirection) {
		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			query = query
					.order(direction(sortDirection) + sortable.map(sortBy));
		}

		return query;
	}

	private static <T> Field key (Class<T> c) {
		Field k = null;

		Field[] fs = c.getDeclaredFields();

		for (Field field : fs) {
			Annotation[] as = field.getAnnotations();

			for (Annotation annotation : as) {
				if (annotation instanceof Id) {
					try {
						k = field;
					} catch (IllegalArgumentException ex) {
						throw new RuntimeException(ex);
					}

					break;
				}
			}

			if (k != null) {
				break;
			}
		}

		return k;
	}
}

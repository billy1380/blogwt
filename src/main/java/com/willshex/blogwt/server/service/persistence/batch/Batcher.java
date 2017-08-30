//
//  Batcher.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.persistence.batch;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keysToIds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.datatype.DataType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Batcher {

	private static final Logger LOG = Logger.getLogger(Batcher.class.getName());

	@FunctionalInterface
	public static interface Id<S, T> {
		S id (T t);
	}

	@FunctionalInterface
	public static interface Has<T, U> {
		Key<U> key (T t);
	}

	@FunctionalInterface
	public static interface HasSet<T, U> {
		void set (T t, U u);
	}

	@FunctionalInterface
	public static interface HasMany<T, U> {
		List<Key<U>> keys (T t);
	}

	@FunctionalInterface
	public static interface HasManyAdd<T, U> {
		void add (T t, U u);
	}

	@FunctionalInterface
	public static interface BatchGetter<U> {
		List<U> get (Collection<Long> ids);
	}

	public static <T extends DataType, U> void lookup (T t, Has<T, U> h,
			HasSet<T, U> g, BatchGetter<U> b) {
		lookup(Arrays.asList(t), PersistenceHelper::id, h, g, b);
	}

	public static <T extends DataType, U> void lookup (T t, Id<Long, U> k,
			Has<T, U> h, HasSet<T, U> g, BatchGetter<U> b) {
		lookup(Arrays.asList(t), k, h, g, b);
	}

	public static <T extends DataType, U> void lookup (Collection<T> c,
			Has<T, U> h, HasSet<T, U> g, BatchGetter<U> b) {
		lookup(c, PersistenceHelper::id, h, g, b);
	}

	public static <T extends DataType, U> void lookup (Collection<T> c,
			Id<Long, U> k, Has<T, U> h, HasSet<T, U> g, BatchGetter<U> b) {
		if (c != null && !c.isEmpty()) {
			Map<Long, List<T>> lookup = new HashMap<>();
			List<T> sub;
			Long id;
			for (T t : c) {
				id = keyToId(h.key(t));

				if (id == null) {
					if (LOG.isLoggable(Level.FINE)) {
						LOG.fine("Batch lookup for object found no key");
					}
				} else {
					if ((sub = lookup.get(id)) == null) {
						lookup.put(id, sub = new ArrayList<>());
					}

					sub.add(t);
				}
			}

			List<U> us = b.get(lookup.keySet());

			for (U u : us) {
				sub = lookup.get(k.id(u));

				for (T t : sub) {
					g.set(t, u);
				}
			}
		}
	}

	public static <T extends DataType, U> void lookup (T t, HasMany<T, U> h,
			HasManyAdd<T, U> a, BatchGetter<U> b) {
		lookup(Arrays.asList(t), PersistenceHelper::id, h, a, b);
	}

	public static <T extends DataType, U> void lookup (T t, Id<Long, U> k,
			HasMany<T, U> h, HasManyAdd<T, U> a, BatchGetter<U> b) {
		lookup(Arrays.asList(t), k, h, a, b);
	}

	public static <T extends DataType, U> void lookup (Collection<T> c,
			HasMany<T, U> h, HasManyAdd<T, U> a, BatchGetter<U> b) {
		lookup(c, PersistenceHelper::id, h, a, b);
	}

	public static <T extends DataType, U> void lookup (Collection<T> c,
			Id<Long, U> k, HasMany<T, U> h, HasManyAdd<T, U> a,
			BatchGetter<U> b) {
		if (c != null && !c.isEmpty()) {
			Map<Long, List<T>> lookup = new HashMap<>();
			List<T> sub;
			List<Long> ids;
			for (T t : c) {
				ids = keysToIds(h.keys(t));

				if (ids == null) {
					if (LOG.isLoggable(Level.FINE)) {
						LOG.fine("Batch lookup for objects found no keys");
					}
				} else {
					for (Long id : ids) {
						if ((sub = lookup.get(id)) == null) {
							lookup.put(id, sub = new ArrayList<>());
						}

						sub.add(t);
					}
				}
			}

			List<U> us = b.get(lookup.keySet());

			for (U u : us) {
				sub = lookup.get(k.id(u));

				for (T t : sub) {
					a.add(t, u);
				}
			}
		}
	}

}

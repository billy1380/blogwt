//
//  Batcher.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.persistence.batch;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Batcher {
	public static interface Has<T, U> {
		Key<U> get (T t);

		void set (T t, U u);

		Long id (U u);
	}

	public static interface BatchGetter<U> {
		List<U> get (Collection<Long> ids);
	}

	public static <T, U> void lookup (Collection<T> c, Has<T, U> h,
			BatchGetter<U> b) {
		if (c != null && !c.isEmpty()) {
			Map<Long, List<T>> lookup = new HashMap<>();
			List<T> sub;
			Long id;
			for (T t : c) {
				id = keyToId(h.get(t));

				if ((sub = lookup.get(id)) == null) {
					lookup.put(id, sub = new ArrayList<>());
				}

				sub.add(t);
			}

			List<U> us = b.get(lookup.keySet());

			for (U u : us) {
				sub = lookup.get(h.id(u));

				for (T t : sub) {
					h.set(t, u);
				}
			}
		}
	}
}

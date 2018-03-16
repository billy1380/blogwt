//
//  DataTypeHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.DataType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DataTypeHelper {
	public static <T extends DataType> boolean same (T a, T b) {
		return a != null && b != null && a.id != null && b.id != null
				&& a.id.longValue() == b.id.longValue();
	}

	public static <T extends DataType> boolean same (Key<T> a, T b) {
		return a != null && b != null && b.id != null
				&& a.getId() == b.id.longValue();
	}

	public static <T extends DataType> boolean same (Key<T> a, Key<T> b) {
		return a != null && b != null && a.getId() == b.getId();
	}

	public static <T extends DataType> boolean same (T a, Long id) {
		return a != null && id != null && a.id != null
				&& a.id.longValue() == id.longValue();
	}

	public static <T> Map<Long, T> map (List<T> data, Function<T, Long> id) {
		Map<Long, T> map = new HashMap<>();

		for (T t : data) {
			map.put(id.apply(t), t);
		}

		return map;
	}

	public static <T extends DataType> Map<Long, T> map (List<T> data) {
		return map(data, t -> t.id);
	}
}

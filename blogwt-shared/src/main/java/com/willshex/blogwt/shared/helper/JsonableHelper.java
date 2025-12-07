//
//  JsonableHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.willshex.gson.shared.Jsonable;
import com.willshex.utility.JsonUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class JsonableHelper {

	public static <T extends Jsonable> T copy (T from, T to) {
		to.fromJson(JsonUtils.cleanJson(from.toJson().toString()));
		return to;
	}

	public static <T extends Jsonable> Collection<T> values (
			Map<String, T> map) {
		return map == null ? null : map.values();
	}

	public static <T extends Jsonable> List<T> fromJson (List<String> json,
			Supplier<T> instancer) {
		List<T> ts = null;

		if (json != null) {
			ts = json.stream().map(j -> fromJson(j, instancer))
					.collect(Collectors.toList());
		}

		return ts;
	}

	public static <T extends Jsonable> T fromJson (String json,
			Supplier<T> instancer) {
		T t = null;

		if (json != null && !json.trim().isEmpty()) {
			t = instancer.get();
			t.fromJson(json);
		}

		return t;
	}

	public static <T extends Jsonable> void addFromJson (List<T> list,
			String json, Supplier<T> instancer) {
		T t = fromJson(json, instancer);

		if (t != null) {
			list.add(t);
		}
	}

	public static String jsonForJsVar (Jsonable jsonable) {
		return null == jsonable ? null
				: jsonable.toString().replace("'", "\\'")
						.replace("\\n", "\\\\n").replace("\\\"", "\\\\\"");
	}

}

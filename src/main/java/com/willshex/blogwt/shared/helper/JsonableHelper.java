//
//  JsonableHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.Collection;
import java.util.Map;

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

}

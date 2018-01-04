//
//  DataTypeHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

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
}

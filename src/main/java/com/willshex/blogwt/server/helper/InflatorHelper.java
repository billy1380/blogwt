//
//  InflatorHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author William Shakour (billy1380)
 *
 */
public class InflatorHelper {

	private static final Logger LOG = Logger.getLogger(InflatorHelper.class
			.getName());

	/**
	 * Replace variables in the format ${x.y} with values located in the map
	 * @param values to inflate mixed with
	 * @param mixed a string containing a mix of text and inflatable values
	 * @return inflated string
	 */
	public static String inflate (Map<String, ?> values, String mixed) {
		StringBuffer inflated = new StringBuffer();

		int count = mixed.length();

		boolean foundDolar = false, foundStart = false;
		StringBuffer inflatable = new StringBuffer();
		char c;
		for (int i = 0; i < count; i++) {
			c = mixed.charAt(i);

			if (foundDolar) {
				if (c == '{') {
					foundStart = true;
					inflatable.setLength(0);
				} else {
					inflated.append('$');
					inflated.append(c);
				}

				foundDolar = false;
			} else if (!foundDolar && c == '$') {
				foundDolar = true;
			} else if (foundStart && c == '}') {
				foundStart = false;

				String[] path = null;
				if (inflatable.length() > 0) {
					path = inflatable.toString().split("\\.");
				}

				Object o = null;
				if (path != null && path.length > 0) {
					o = values.get(path[0]);

					for (int j = 1; j < path.length; j++) {
						o = value(o, path[j]);
					}

					if (o != null) {
						inflated.append(o.toString());
					}
				}
			} else if (foundStart) {
				inflatable.append(c);
			} else {
				inflated.append(c);
			}
		}

		return inflated.toString();
	}

	private static Object value (Object object, String name) {
		Object value = null;

		if (object != null) {
			List<Field> fields = new ArrayList<Field>();
			Class<? extends Object> type = object.getClass();

			do {
				fields.addAll(Arrays.asList(type.getDeclaredFields()));
			} while ((type = type.getSuperclass()) != null);

			for (Field field : fields) {
				if (name.equals(field.getName())) {
					try {
						value = field.get(object);
						break;
					} catch (IllegalAccessException | IllegalArgumentException e) {
						LOG.log(Level.WARNING, String.format(
								"Error accessing field [%s]", field.getName()),
								e);
					}
				}
			}
		}

		return value;
	}
}

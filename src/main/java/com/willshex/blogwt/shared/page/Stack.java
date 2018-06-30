//
//  Stack.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.page;

import java.util.Arrays;

import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Stack {
	public static final String NEXT_KEY = "nextgoto=";
	public static final String PREVIOUS_KEY = "prevgoto=";

	private String allParts;
	private String[] parts;

	private Stack previous;
	private Stack next;

	private Stack (String value) {
		if (value != null) {
			allParts = value;
			parts = allParts.split("/");

			for (String part : parts) {
				String[] parameters;
				if (next == null && (parameters = Stack.decode(NEXT_KEY,
						part)) != null) {
					next = new Stack(
							StringUtils.join(Arrays.asList(parameters), "/"));
				} else if (previous == null && (parameters = Stack
						.decode(PREVIOUS_KEY, part)) != null) {
					previous = new Stack(
							StringUtils.join(Arrays.asList(parameters), "/"));
				}
			}
		}
	}

	public String getPage () {
		return parts.length > 0 ? parts[0] : null;
	}

	public String getPageSlug () {
		String slug = null;
		if (parts.length > 0) {
			if (parts[0] != null && parts[0].length() > 0) {
				if (parts[0].charAt(0) == '!') {
					slug = parts[0].substring(1);
				} else {
					slug = parts[0];
				}
			}
		}

		return slug;
	}

	public String getAction () {
		return parts.length > 1 ? parts[1] : null;
	}

	public String getParameter (int index) {
		return parts.length > (2 + index) ? parts[2 + index] : null;
	}

	public static Stack parse (String value) {
		return new Stack(value);
	}

	/**
	 * @return
	 */
	public boolean hasAction () {
		return getAction() != null;
	}

	public boolean hasPage () {
		return getPage() != null;
	}

	@Override
	public String toString () {
		return allParts;
	}

	public String toString (int fromPart) {
		return parts == null ? ""
				: StringUtils.join(
						Arrays.asList(parts).subList(fromPart, parts.length),
						"/");
	}

	public String toString (String... param) {
		return toString() + "/" + StringUtils.join(Arrays.asList(param), "/");
	}

	public String toString (int fromPart, String... param) {
		return toString(fromPart) + "/"
				+ StringUtils.join(Arrays.asList(param), "/");
	}

	public String asParameter () {
		return Stack.encode(null, allParts);
	}

	public String asNextParameter () {
		return Stack.encode(NEXT_KEY, allParts.split("/"));
	}

	public String asPreviousParameter () {
		return Stack.encode(PREVIOUS_KEY, allParts.split("/"));
	}

	/**
	 * @return
	 */
	public boolean hasNext () {
		return next != null;
	}

	/**
	 * @return
	 */
	public Stack getNext () {
		return next;
	}

	/**
	 * @return
	 */
	public boolean hasPrevious () {
		return previous != null;
	}

	/**
	 * @return
	 */
	public Stack getPrevious () {
		return previous;
	}

	public static String encode (String name, String... values) {
		String parameters = "";

		if (values != null && values.length > 0) {
			parameters = StringUtils.join(Arrays.asList(values), "$");
		}

		return (name == null || name.length() == 0) ? parameters
				: (name + parameters);
	}

	public static String[] decode (String name, String encoded) {
		String content;
		String[] splitContent = null;

		if (encoded != null && encoded.length() > 0
				&& encoded.startsWith(name)) {
			content = encoded.substring(name.length());

			if (content != null && content.length() > 0) {
				splitContent = content.split("\\$");
			}
		}

		return splitContent;
	}

	public int getParameterCount () {
		int count = parts.length - 2;
		return count > 0 ? count : 0;
	}
}

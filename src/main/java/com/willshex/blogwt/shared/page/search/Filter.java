//
//  Filter.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 23 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.page.search;

import java.util.Arrays;

import com.google.gwt.core.shared.GWT;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Filter {

	public static interface Query {
		String from (Filter filter);
	}

	public static final String QUERY = "query";

	public String type = null;
	public String query = null;

	public static Filter fromStack (Stack stack) {
		final int count = Math.min(stack.getParameterCount(), 1);
		String parameter;
		Filter filter = new Filter();
		for (int i = 0; i < count; i++) {
			parameter = StringUtils.urldecode(stack.getParameter(i));
			switch (i) {
			case 0:
				filter.query = parameter;
				break;
			}
		}

		filter.type = stack.getPageSlug();

		return filter;
	}

	public String urlParameters () {
		return hasItems() ? StringUtils.join(Arrays.asList(QUERY, query), "/")
				: "";
	}

	public String url () {
		return hasItems()
				? StringUtils.join(Arrays.asList(type, QUERY, query), "/")
				: (type == null ? "" : type);
	}

	@Override
	public boolean equals (Object obj) {
		GWT.log(url());
		GWT.log(((Filter) obj).url());
		return obj != null && obj instanceof Filter
				? url().equals(((Filter) obj).url())
				: super.equals(obj);
	}

	public Filter copy () {
		Filter copy = new Filter();
		copy.query = query;
		copy.type = type;
		return copy;
	}

	public boolean hasItems () {
		return query != null && query.trim().length() > 0;
	}

	public Filter query (String value) {
		query = value;
		return this;
	}

	public Filter type (String value) {
		type = value;
		return this;
	}
}

//
//  StaticTag.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import com.willshex.blogwt.server.api.blog.BlogApi;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
class StaticTag extends StaticPosts {

	public StaticTag (Stack stack) {
		super(stack);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#appendPage(java.lang.
	 * StringBuffer) */
	@Override
	protected void appendPage (StringBuffer markup) {
		String tag;
		if ((tag = stack.getAction()) != null) {
			markup.append("<h2>Posts tagged with <strong>&apos;" + tag
					+ "&apos;</strong></h2>");

			BlogApi api = new BlogApi();

			GetPostsRequest input = input(GetPostsRequest.class)
					.pager(PagerHelper.createDefaultPager()).tag(tag);

			GetPostsResponse output = api.getPosts(input);

			if (output.status == StatusType.StatusTypeSuccess
					&& output.posts != null) {
				showPosts(output.posts, markup);
			} else {
				markup.append(output.error.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#title() */
	@Override
	protected String title () {
		return "Tag '" + stack.getAction() + "'";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticPosts#canCreate() */
	@Override
	public boolean canCreate () {
		return stack != null && stack.hasAction()
				&& stack.getAction().length() != 0;
	}

}
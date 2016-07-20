//
//  StaticPost.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import com.willshex.blogwt.server.api.blog.action.GetPostActionHandler;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
class StaticPost extends StaticTemplate {

	private GetPostResponse output;

	public StaticPost (Stack stack) {
		super(stack);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#appendPage(java.lang.
	 * StringBuffer) */
	@Override
	protected void appendPage (StringBuffer markup) {
		Post post = ensurePost();

		if (post != null) {
			markup.append(process("##" + post.title));

			markup.append("<div><span>");
			markup.append(DateTimeHelper.ago(post.published));
			markup.append("</span> by <img src=\"");
			markup.append(post.author.avatar);
			markup.append("?s=");
			markup.append(UserHelper.AVATAR_HEADER_SIZE);
			markup.append("&default=retro\" /> ");
			markup.append(UserHelper.handle(post.author));

			if (post.content != null) {
				markup.append(process(post.content.body));
			}

			if (post.tags != null) {
				markup.append("<ul>");
				for (String tag : post.tags) {
					markup.append("<li><a href=\"");
					markup.append("#" + PageType.TagPostsPageType
							.asTargetHistoryToken(tag));
					markup.append("\">");
					markup.append(tag);
					markup.append("</a></li>");
				}
				markup.append("</ul>");
			}
		} else {
			markup.append("Page not found.");
		}

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#title() */
	@Override
	protected String title () {
		Post post;
		return (post = ensurePost()) == null ? "Error" : post.title;
	}

	private Post ensurePost () {
		if (output == null) {
			lookupPost();
		}

		return output.post;
	}

	private void lookupPost () {
		GetPostRequest input = input(GetPostRequest.class)
				.post(new Post().slug(stack.getAction()));
		output = (new GetPostActionHandler()).handle(input);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.PageMarkup#canCreate() */
	@Override
	public boolean canCreate () {
		return stack != null && stack.hasAction()
				&& stack.getAction().length() > 0;
	}
}
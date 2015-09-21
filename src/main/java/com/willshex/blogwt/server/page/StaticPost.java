//
//  StaticPost.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import com.willshex.blogwt.server.api.blog.BlogApi;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
class StaticPost extends StaticTemplate {

	public StaticPost (Stack stack) {
		super(stack);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.page.StaticTemplate#appendPage(java.lang.
	 * StringBuffer) */
	@Override
	protected void appendPage (StringBuffer markup) {
		String slug = null;
		if ((slug = stack.getAction()) != null) {
			BlogApi api = new BlogApi();

			GetPostRequest input = input(GetPostRequest.class).post(
					new Post().slug(slug));
			GetPostResponse output = api.getPost(input);

			if (output.status == StatusType.StatusTypeSuccess
					&& output.post != null) {

				markup.append(process("##" + output.post.title));

				markup.append("<div><span>");
				markup.append(DateTimeHelper.ago(output.post.published));
				markup.append("</span> by <img src=\"");
				markup.append(output.post.author.avatar);
				markup.append("?s=");
				markup.append(UserHelper.AVATAR_HEADER_SIZE);
				markup.append("&default=retro\" /> ");
				markup.append(UserHelper.handle(output.post.author));

				if (output.post.content != null) {
					markup.append(process(output.post.content.body));
				}

				if (output.post.tags != null) {
					markup.append("<ul>");
					for (String tag : output.post.tags) {
						markup.append("<li><a href=\"");
						markup.append("#"
								+ PageType.TagPostsPageType
										.asTargetHistoryToken(tag));
						markup.append("\">");
						markup.append(tag);
						markup.append("</a></li>");
					}
					markup.append("</ul>");
				}
			} else {
				markup.append(output.error.toString());
			}

		}
	}
}
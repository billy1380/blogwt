//
//  StaticPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import com.willshex.blogwt.server.api.page.action.GetPageActionHandler;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
class StaticPage extends StaticTemplate {

	private String slug = null;
	private GetPageResponse output;

	public StaticPage (Stack stack) {
		super(stack);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#appendPage(java.lang.
	 * StringBuffer) */
	@Override
	protected void appendPage (StringBuffer markup) {
		Page page = ensurePage();

		if (page != null && page.posts != null) {
			for (Post post : page.posts) {
				if (post.content != null && post.content.body != null) {
					markup.append("<a name=\"!");
					markup.append(page.slug);
					markup.append("/");
					markup.append(post.slug);
					markup.append("\"></a>");
					markup.append("<section><div>");
					markup.append(process(post.content.body));
					markup.append("</div></section>");
				}
			}
		} else {
			markup.append("Page not found.");
		}
	}

	private Page ensurePage () {
		if (output == null) {
			lookupPage();
		}

		return output.page;
	}

	private void lookupPage () {

		GetPageRequest input = input(GetPageRequest.class)
				.includePosts(Boolean.TRUE).page(new Page().slug(slug));

		output = (new GetPageActionHandler()).handle(input);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#title() */
	@Override
	protected String title () {
		Page page = ensurePage();
		return page == null ? "Error" : page.title;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.StaticTemplate#isValidStack() */
	@Override
	public boolean canCreate () {
		if (PageType
				.fromString(stack.getPage()) == PageType.PageDetailPageType) {
			slug = stack.getAction();
		} else {
			slug = stack.getPageSlug();
		}

		if (slug == null) {
			if (home != null) {
				slug = home.slug;
			}
		}

		return slug != null;
	}

}

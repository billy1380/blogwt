//
//  StaticPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import java.io.IOException;

import org.markdown4j.server.IncludePlugin;
import org.markdown4j.server.MarkdownProcessor;

import com.willshex.blogwt.server.api.page.PageApi;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
class StaticPage extends StaticTemplate implements PageMarkup {

	public StaticPage (Stack stack) {
		super(stack);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.PageMarkup#asString() */
	@Override
	public String asString () {
		StringBuffer markup = new StringBuffer();

		appendHeader(markup);

		String slug = null;
		if (PageType.fromString(stack.getPage()) == PageType.PageDetailPageType) {
			slug = stack.getAction();
		} else {
			slug = stack.getPageSlug();
		}

		if (slug == null) {
			if (home != null) {
				slug = home.slug;
			}
		}

		if (slug == null) {
			// Ideally here we would like to redirect need to figure out how to do that
		} else {
			PageApi api = new PageApi();

			GetPageRequest input = input(GetPageRequest.class).includePosts(
					Boolean.TRUE).page(new Page().slug(slug));

			GetPageResponse output = api.getPage(input);

			if (output.status == StatusType.StatusTypeSuccess
					&& output.page != null && output.page.posts != null) {
				MarkdownProcessor processor = new MarkdownProcessor();
				processor.registerPlugins(new IncludePlugin());

				for (Post post : output.page.posts) {
					if (post.content != null && post.content.body != null) {
						markup.append("<a name=\"!");
						markup.append(output.page.slug);
						markup.append("/");
						markup.append(post.slug);
						markup.append("\"></a>");

						markup.append("<section><div>");

						try {
							markup.append(processor.process(post.content.body));
						} catch (IOException e) {
							throw new RuntimeException(e);
						}

						markup.append("</div></section>");
					}
				}
			} else {
				markup.append(output.error.toString());
			}
		}

		appendFooter(markup);

		return markup.toString();
	}

}

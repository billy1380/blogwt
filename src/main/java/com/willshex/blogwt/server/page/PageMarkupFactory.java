//
//  PageMarkupFactory.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import com.willshex.blogwt.shared.page.PageType;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageMarkupFactory {

	private static PageMarkup POSTS = new StaticPosts(null);

	public static PageMarkup createFromStack (Stack stack) {
		PageType pageType = PageType.fromString(stack.getPage());
		pageType = (pageType == null ? PageType.PageDetailPageType : pageType);
		PageMarkup pageMarkup = null;

		switch (pageType) {
		case PageDetailPageType:
			pageMarkup = new StaticPage(stack);
			break;
//		case PostsPageType:
//			pageMarkup = new StaticPosts(stack);
//			break;
		case PostDetailPageType:
			pageMarkup = new StaticPost(stack);
			break;
		case TagPostsPageType:
			pageMarkup = new StaticTag(stack);
			break;
		default:
			break;
		}

		return pageMarkup != null && pageMarkup.canCreate() ? pageMarkup : POSTS;
	}

}

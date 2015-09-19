//
//  StaticPosts.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.page;

import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
class StaticPosts extends StaticTemplate implements PageMarkup {

	public StaticPosts (Stack stack) {
		super(stack);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.page.PageMarkup#asString() */
	@Override
	public String asString () {
		return "";
	}

}
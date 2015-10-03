//
//  PostOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostOracle extends SuggestOracle<Post> {

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (Request request, Callback callback) {
		this.foundItems(request, callback, Collections.<Post> emptyList());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Post item) {
		return item.title;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Post item) {
		return item.id.toString();
	}

}

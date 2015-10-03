//
//  RoleOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 3 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.willshex.blogwt.shared.api.datatype.Role;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RoleOracle extends SuggestOracle<Role> {

	public interface NameDescriptionTemplates extends SafeHtmlTemplates {

		NameDescriptionTemplates INSTANCE = GWT
				.create(NameDescriptionTemplates.class);

		@Template("{0}<div class=\"smaller text-muted\">{1}</div>")
		SafeHtml description (String name, String description);

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (Request request, Callback callback) {
		this.foundItems(request, callback, Collections.<Role> emptyList());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Role item) {
		return NameDescriptionTemplates.INSTANCE.description(item.name,
				item.description).asString();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Role item) {
		return item.code;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.SuggestOracle#isDisplayStringHTML() */
	@Override
	public boolean isDisplayStringHTML () {
		return true;
	}

}

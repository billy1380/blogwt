//
//  PermissionOracle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 3 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle;

import java.util.Collections;

import com.willshex.blogwt.client.oracle.RoleOracle.NameDescriptionTemplates;
import com.willshex.blogwt.shared.api.datatype.Permission;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionOracle extends SuggestOracle<Permission> {

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt
	 * .user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (Request request, Callback callback) {
		this.foundItems(request, callback, Collections.<Permission> emptyList());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getDisplayString(java
	 * .lang.Object) */
	@Override
	protected String getDisplayString (Permission item) {
		return NameDescriptionTemplates.INSTANCE.description(item.name,
				item.description).asString();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#getReplacementString
	 * (java.lang.Object) */
	@Override
	protected String getReplacementString (Permission item) {
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

//
//  InlineBootstrapGwtCellListStyle.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 March 2014.
//  Copyright © 2014 WillShex Limited. All rights reserved.
//
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Style;

/**
 * @author billy1380
 * 
 */
public interface InlineBootstrapGwtCellList extends CellList.Resources {
	public static final InlineBootstrapGwtCellList INSTANCE = GWT
			.create(InlineBootstrapGwtCellList.class);

	public interface InlineBootstrapGwtCellListStyle extends Style {};

	@Source("res/inline-bootstrap-gwt-celllist.gss")
	InlineBootstrapGwtCellListStyle cellListStyle ();
};

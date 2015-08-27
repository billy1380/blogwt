//
//  BootstrapGwtCellTree.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 26 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface BootstrapGwtCellTree extends CellTree.Resources {
	public static final BootstrapGwtCellTree INSTANCE = GWT
			.create(BootstrapGwtCellTree.class);

	public interface BootstrapGwtCellTreeStyle extends CellTree.Style {};

	@Source("res/bootstrap-gwt-celltree.gss")
	BootstrapGwtCellTreeStyle cellTreeStyle ();
};
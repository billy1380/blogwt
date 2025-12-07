//
//  BootstrapGwtCellTable.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 21 Dec 2014.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface BootstrapGwtCellTable extends CellTable.Resources {
	public static final BootstrapGwtCellTable INSTANCE = GWT.create(BootstrapGwtCellTable.class);

	public interface BootstrapGwtCellTableStyle extends CellTable.Style {};

	@Source("res/bootstrap-gwt-celltable.gss")
	BootstrapGwtCellTableStyle cellTableStyle();
};

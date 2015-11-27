//
//  Resources.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2014 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface Resources extends ClientBundle {
	public interface ResourceStyles extends CssResource {
		String primaryLoader ();

		String defaultLoader ();

		String successLoader ();

		String infoLoader ();

		String warningLoader ();

		String dangerLoader ();

		String image ();

		String emoji ();

		String brand ();

		String largeBrand ();

		String internalLink ();

		String externalLink ();

		String blink ();
	}

	public static final Resources RES = GWT.create(Resources.class);

	public static final CellTable.Resources CELL_TABLE_RES = GWT
			.create(CellTable.Resources.class);

	@Source("res/styles.gss")
	ResourceStyles styles ();

	@Source("res/primary-loader.gif")
	ImageResource primaryLoader ();

	@Source("res/default-loader.gif")
	ImageResource defaultLoader ();

	@Source("res/success-loader.gif")
	ImageResource successLoader ();

	@Source("res/info-loader.gif")
	ImageResource infoLoader ();

	@Source("res/warning-loader.gif")
	ImageResource warningLoader ();

	@Source("res/danger-loader.gif")
	ImageResource dangerLoader ();

	@Source("res/brand.png")
	ImageResource brand ();

	@Source("res/large-brand.png")
	ImageResource largeBrand ();

}

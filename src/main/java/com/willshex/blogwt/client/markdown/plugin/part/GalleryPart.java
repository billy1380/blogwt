//
//  GalleryPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Jan 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GalleryPart extends Composite {

	private static GalleryPartUiBinder uiBinder = GWT
			.create(GalleryPartUiBinder.class);

	interface GalleryPartUiBinder extends UiBinder<Widget, GalleryPart> {}

	public GalleryPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

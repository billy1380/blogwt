//
//  PostNavPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Dec 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostNavPart extends Composite {

	private static PostNavPartUiBinder uiBinder = GWT
			.create(PostNavPartUiBinder.class);

	interface PostNavPartUiBinder extends UiBinder<Widget, PostNavPart> {}

	public PostNavPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

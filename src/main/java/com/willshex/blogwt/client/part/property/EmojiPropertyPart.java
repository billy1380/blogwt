//
//  EmojiPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 31 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author billy1380
 *
 */
public class EmojiPropertyPart extends AbstractPropertyPart {

	private static EmojiPropertyPartUiBinder uiBinder = GWT
			.create(EmojiPropertyPartUiBinder.class);

	interface EmojiPropertyPartUiBinder extends
			UiBinder<Widget, EmojiPropertyPart> {}

	public EmojiPropertyPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setValue (String value, boolean fireEvents) {

	}

	@Override
	public void setName (String name) {

	}

	@Override
	public String getName () {
		return null;
	}

}

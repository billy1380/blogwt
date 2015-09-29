//
//  ProtectedStringPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 29 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ProtectedStringPropertyPart extends StringPropertyPart {

	private static ProtectedStringPropertyPartUiBinder uiBinder = GWT
			.create(ProtectedStringPropertyPartUiBinder.class);

	interface ProtectedStringPropertyPartUiBinder extends
			UiBinder<Widget, ProtectedStringPropertyPart> {}

	public ProtectedStringPropertyPart () {
		super(false);
		initWidget(uiBinder.createAndBindUi(this));
	}

}

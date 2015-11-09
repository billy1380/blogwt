//
//  TextAreaPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 9 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TextAreaPart extends Composite {

	private static TextAreaPartUiBinder uiBinder = GWT
			.create(TextAreaPartUiBinder.class);

	interface TextAreaPartUiBinder extends UiBinder<Widget, TextAreaPart> {}

	public @UiField Element elName;
	public @UiField TextArea txtValue;
	public @UiField HTMLPanel pnlValidationNote;
	
	public TextAreaPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

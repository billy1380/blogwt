//
//  TextBoxPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 7 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TextBoxPart extends Composite implements FormField {

	private static TextBoxPartUiBinder uiBinder = GWT
			.create(TextBoxPartUiBinder.class);

	interface TextBoxPartUiBinder extends UiBinder<Widget, TextBoxPart> {}

	public @UiField Element elName;
	public @UiField TextBox txtValue;
	public @UiField HTMLPanel pnlValidationNote;

	public TextBoxPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.part.form.FormField#isValid()
	 */
	@Override
	public boolean isValid () {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.part.form.FormField#showError()
	 */
	@Override
	public void showError () {
		// TODO Auto-generated method stub
		
	}

}

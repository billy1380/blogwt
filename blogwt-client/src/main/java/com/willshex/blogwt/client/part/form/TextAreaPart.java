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
public class TextAreaPart extends Composite implements FormField {

	private static TextAreaPartUiBinder uiBinder = GWT
			.create(TextAreaPartUiBinder.class);

	interface TextAreaPartUiBinder extends UiBinder<Widget, TextAreaPart> {}

	public @UiField Element elName;
	public @UiField TextArea txtValue;
	public @UiField HTMLPanel pnlValidationNote;
	private String error;

	public TextAreaPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@Override
	public boolean isValid () {
		boolean valid = true;
		String value = value();

		if (value.length() < 1) {
			error = name() + " cannot be blank (1 - 2000)";
			valid = false;
		} else if (value.length() > 2048) {
			error = name() + " is too long (1 - 2000)";
			valid = false;
		}

		return valid;
	}
	@Override
	public void showError () {
		if (error != null) {
			pnlValidationNote.getElement().setInnerText(error);
			pnlValidationNote.setVisible(true);
			addStyleName("has-error");
		}
	}
	@Override
	public void hideError () {
		error = null;
		pnlValidationNote.setVisible(false);
		removeStyleName("has-error");
	}
	@Override
	public String value () {
		return txtValue.getValue();
	}
	@Override
	public String name () {
		return elName.getInnerText();
	}

	public void setLarge (boolean large) {
		if (large) {
			addStyleName("form-group-lg");
		} else {
			removeStyleName("form-group-lg");
		}
	}

}

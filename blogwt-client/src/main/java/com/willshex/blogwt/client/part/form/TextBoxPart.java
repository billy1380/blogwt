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
	private String error;

	public TextBoxPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#isValid() */
	@Override
	public boolean isValid () {
		boolean valid = true;
		String value = value();

		if (value.length() < 1) {
			error = name() + " cannot be blank (1 - 500)";
			valid = false;
		} else if (value.length() > 512) {
			error = name() + " is too long (1 - 500)";
			valid = false;
		}

		return valid;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#showError() */
	@Override
	public void showError () {
		if (error != null) {
			pnlValidationNote.getElement().setInnerText(error);
			pnlValidationNote.setVisible(true);
			addStyleName("has-error");
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#hideError() */
	@Override
	public void hideError () {
		error = null;
		pnlValidationNote.setVisible(false);
		removeStyleName("has-error");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#value() */
	@Override
	public String value () {
		return txtValue.getValue();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.part.form.FormField#name() */
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

//
//  ListBoxPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ListBoxPart extends Composite implements FormField {

	private static ListBoxPartUiBinder uiBinder = GWT
			.create(ListBoxPartUiBinder.class);

	interface ListBoxPartUiBinder extends UiBinder<Widget, ListBoxPart> {}

	public @UiField Element elName;
	public @UiField ListBox lbxValue;
	public @UiField HTMLPanel pnlValidationNote;
	private String error;

	public ListBoxPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}
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
		return lbxValue.getSelectedValue();
	}
	@Override
	public String name () {
		return elName.getInnerText();
	}

}

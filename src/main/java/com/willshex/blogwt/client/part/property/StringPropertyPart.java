//
//  StringPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class StringPropertyPart extends Composite {

	@UiField Element elDescription;
	@UiField Element elName;
	@UiField TextBox txtValue;
	@UiField HTMLPanel pnlValueNote;

	private static StringPropertyPartUiBinder uiBinder = GWT
			.create(StringPropertyPartUiBinder.class);

	interface StringPropertyPartUiBinder extends
			UiBinder<Widget, StringPropertyPart> {}

	public StringPropertyPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @param description
	 */
	public void setDescription (String description) {
		elDescription.setInnerText(description);
		UiHelper.addPlaceholder(txtValue, description);
	}

	/**
	 * @param name
	 */
	public void setName (String name) {
		elName.setInnerText(name);
	}

	/**
	 * @param value
	 */
	public void setValue (String value) {
		txtValue.setValue(value);
	}

	/**
	 */
	public void setAutofocus () {
		UiHelper.autoFocus(txtValue);
	}

}

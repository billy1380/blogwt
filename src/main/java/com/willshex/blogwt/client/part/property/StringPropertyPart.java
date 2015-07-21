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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class StringPropertyPart extends AbstractPropertyPart {

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
	 */
	public void setAutofocus () {
		UiHelper.autoFocus(txtValue);
	}

	@UiHandler("txtValue")
	void onTextValueChanged (ValueChangeEvent<String> vce) {
		setValue(vce.getValue(), true);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object,
	 * boolean) */
	@Override
	public void setValue (String value, boolean fireEvents) {
		if (value == null) {
			value = "";
		}

		String oldValue = getValue();

		txtValue.setValue(value);
		this.value = value;

		if (value.equals(oldValue)) { return; }
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#getValue() */
	@Override
	public String getValue () {
		return value;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasName#getName() */
	@Override
	public String getName () {
		return elName.getInnerText();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String) */
	@Override
	public void setName (String name) {
		elName.setInnerText(name);
	}

}

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
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class StringPropertyPart extends AbstractPropertyPart implements
		Focusable {

	@UiField Element elDescription;
	@UiField Element elName;
	@UiField TextBoxBase txtValue;
	@UiField HTMLPanel pnlValueNote;

	private static StringPropertyPartUiBinder uiBinder = GWT
			.create(StringPropertyPartUiBinder.class);

	interface StringPropertyPartUiBinder extends
			UiBinder<Widget, StringPropertyPart> {}

	protected StringPropertyPart (boolean init) {
		if (init) {
			initWidget(uiBinder.createAndBindUi(this));
		}
	}

	public StringPropertyPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private TextBoxBase textBox () {
		return txtValue;
	}

	/**
	 * @param description
	 */
	public void setDescription (String description) {
		elDescription.setInnerText(description);
		UiHelper.addPlaceholder(textBox(), description);
	}

	/**
	 */
	public void setAutofocus () {
		UiHelper.autoFocus(textBox());
	}

	@UiHandler({ "txtValue" })
	void onTextValueChanged (ValueChangeEvent<String> vce) {
		setValue(vce.getValue(), true);
	}
	@Override
	public void setValue (String value, boolean fireEvents) {
		if (value == null) {
			value = "";
		}

		String oldValue = getValue();

		textBox().setValue(value);
		this.value = value;

		if (value.equals(oldValue)) { return; }
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}
	@Override
	public String getName () {
		return elName.getInnerText();
	}
	@Override
	public void setName (String name) {
		elName.setInnerText(name);
	}
	@Override
	public int getTabIndex () {
		return textBox().getTabIndex();
	}
	@Override
	public void setAccessKey (char key) {
		textBox().setAccessKey(key);
	}
	@Override
	public void setFocus (boolean focused) {
		textBox().setFocus(focused);
	}
	@Override
	public void setTabIndex (int index) {
		textBox().setTabIndex(index);
	}

}

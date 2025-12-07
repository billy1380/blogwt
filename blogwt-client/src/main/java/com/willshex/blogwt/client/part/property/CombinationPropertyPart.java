//
//  CombinationPropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.shared.helper.PropertyHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CombinationPropertyPart extends AbstractPropertyPart {

	private static CheckBoxPropertyPartUiBinder uiBinder = GWT
			.create(CheckBoxPropertyPartUiBinder.class);

	interface CheckBoxPropertyPartUiBinder
			extends UiBinder<Widget, CombinationPropertyPart> {}

	interface Style extends CssResource {
		String checkbox ();
	}

	@UiField HTMLPanel pnlCheckBoxes;
	@UiField Element elDescription;
	@UiField Element elName;
	@UiField HTMLPanel pnlValueNote;
	@UiField Style style;
	private Map<String, CheckBox> checkBoxes = new HashMap<String, CheckBox>();

	private void onChecked (ValueChangeEvent<Boolean> event) {
		String value = ((CheckBox) event.getSource()).getFormValue();
		if (event.getValue().booleanValue()) {
			if (CombinationPropertyPart.this.value == null
					|| CombinationPropertyPart.this.value.length() == 0
					|| PropertyHelper.NONE_VALUE
							.equals(CombinationPropertyPart.this.value)) {
				setValue(value, true);
			} else if (!CombinationPropertyPart.this.value.contains(value)) {
				setValue(CombinationPropertyPart.this.value + "," + value,
						true);
			}
		} else {
			if (CombinationPropertyPart.this.value != null
					&& CombinationPropertyPart.this.value.length() > 0
					&& !PropertyHelper.NONE_VALUE
							.equals(CombinationPropertyPart.this.value)) {
				if (CombinationPropertyPart.this.value.contains("," + value)) {
					setValue(CombinationPropertyPart.this.value
							.replace("," + value, ""), true);
				} else if (CombinationPropertyPart.this.value
						.contains(value + ",")) {
					setValue(CombinationPropertyPart.this.value
							.replace(value + ",", ""), true);
				} else {
					setValue(PropertyHelper.NONE_VALUE, true);
				}
			}
		}
	}

	public CombinationPropertyPart (Map<String, String> options) {
		initWidget(uiBinder.createAndBindUi(this));
		CheckBox checkBox;
		for (String key : options.keySet()) {
			pnlCheckBoxes.add(checkBox = new CheckBox());
			checkBox.setFormValue(key);
			checkBox.setText(options.get(key));
			checkBox.addValueChangeHandler(this::onChecked);
			checkBox.setStyleName(style.checkbox());
			checkBoxes.put(key, checkBox);
		}
	}
	@Override
	public void setValue (String value, boolean fireEvents) {
		if (value == null) {
			value = "";
		}

		String oldValue = getValue();

		updateCheckBoxes(value);
		this.value = value;

		if (value.equals(oldValue)) { return; }
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}
	@Override
	public void setName (String name) {
		elName.setInnerText(name);
	}
	@Override
	public String getName () {
		return elName.getInnerText();
	}
	@Override
	public void setDescription (String description) {
		elDescription.setInnerText(description);
	}

	private void updateCheckBoxes (String value) {
		String[] parts = null;

		for (CheckBox checkBox : checkBoxes.values()) {
			checkBox.setValue(Boolean.FALSE);
		}

		if (value != null && value.length() != 0
				&& !PropertyHelper.NONE_VALUE.equals(value)) {
			parts = value.split(",");
			CheckBox checkBox;
			for (String part : parts) {
				checkBox = checkBoxes.get(part);
				if (checkBox != null) {
					checkBox.setValue(Boolean.TRUE);
				}
			}
		}
	}
}

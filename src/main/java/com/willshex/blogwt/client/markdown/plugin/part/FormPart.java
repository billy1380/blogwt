//
//  FormPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 5 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin.part;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ResetButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.part.form.TextBoxPart;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormPart extends Composite {

	private static final String TYPE = "type";
	private static final String NAME = "name";
	private static final String DEFAULT_VALUE = "defaultValue";
	private static final String ALLOWED_VALUES = "allowedValues";

	private static class ConfigLine {
		public String type;
		public String name;
		public String defaultValue;
		public List<String> allowedValues;

		public void addAllowedValue (String value) {
			if (allowedValues == null) {
				allowedValues = new ArrayList<String>();
			}

			allowedValues.add(value);
		}
	}

	private static FormPartUiBinder uiBinder = GWT
			.create(FormPartUiBinder.class);

	interface FormPartUiBinder extends UiBinder<Widget, FormPart> {}

	@UiField ResetButton btnReset;
	@UiField SubmitButton btnSubmit;
	@UiField FormPanel frmForm;
	@UiField HTMLPanel pnlFields;

	public FormPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("frmForm")
	void onFormSubmit (SubmitEvent se) {
		if (isValid()) {
			// Call api with fields
		} else {
			showErrors();
		}

		se.cancel();
	}

	@UiHandler("btnReset")
	void onResetClicked (ClickEvent ce) {
		frmForm.reset();
	}

	private boolean isValid () {
		return true;
	}

	private void showErrors () {

	}

	/**
	 * @param line
	 */
	public void addFieldWithLine (String line) {
		if (line.length() > 0) {
			ConfigLine config = parseConfigLine(line);

			switch (config.type) {
			case "text":
				TextBoxPart textBox = new TextBoxPart();
				textBox.elName.setInnerHTML(config.name);
				UiHelper.addPlaceholder(textBox.txtValue, config.name);
				textBox.txtValue.setValue(config.defaultValue);
				pnlFields.add(textBox);
				break;
			case "textarea":
				break;
			}
		}
	}

	private ConfigLine parseConfigLine (String line) {
		String[] params = line.split("/");
		ConfigLine config = new ConfigLine();

		String[] splitParam, allowedValues;
		for (String param : params) {
			splitParam = param.split("=");

			if (splitParam.length == 2) {
				switch (splitParam[0]) {
				case TYPE:
					config.type = splitParam[1];
					break;
				case NAME:
					config.name = splitParam[1];
					break;
				case DEFAULT_VALUE:
					config.defaultValue = splitParam[1];
					break;
				case ALLOWED_VALUES:
					allowedValues = splitParam[1].split(",");

					for (String value : allowedValues) {
						config.addAllowedValue(value);
					}
				}
			}
		}

		return config;
	}
}

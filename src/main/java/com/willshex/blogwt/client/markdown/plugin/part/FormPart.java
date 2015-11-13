//
//  FormPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 5 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.FormController;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.part.form.ListBoxPart;
import com.willshex.blogwt.client.part.form.ReCaptchaPart;
import com.willshex.blogwt.client.part.form.TextAreaPart;
import com.willshex.blogwt.client.part.form.TextBoxPart;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.datatype.Form;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormResponse;
import com.willshex.blogwt.shared.api.page.call.event.SubmitFormEventHandler;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormPart extends Composite implements SubmitFormEventHandler {

	private static final String FORM_CLASS_PARAM_KEY = "formclass";
	private static final String BODY_PANEL_CLASS_PARAM_KEY = "bodyclass";
	private static final String FIELD_PANEL_CLASS_PARAM_KEY = "fieldclass";
	private static final String BUTTON_BAR_CLASS_PARAM_KEY = "buttonclass";

	private static final String TYPE = "type";
	private static final String NAME = "name";
	private static final String DEFAULT_VALUE = "defaultValue";
	private static final String ALLOWED_VALUES = "allowedValues";

	private static final String RECAPTCH_API_KEY = "recaptchaApiKey";

	private static final String TEXT_TYPE = "text";
	private static final String ONE_OPTION_TYPE = "oneOption";
	private static final String LONG_TEXT_TYPE = "longText";
	private static final String CAPTCHA_TYPE = "captcha";

	private static class ConfigLine {
		public String type;
		public String name;
		public String defaultValue;
		public List<String> allowedValues;
		public Map<String, String> parameters;

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
	@UiField HTMLPanel pnlButtons;
	@UiField HTMLPanel pnlBody;
	private HandlerRegistration registration;

	public FormPart () {
		initWidget(uiBinder.createAndBindUi(this));

		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.nextButton("Send"));
	}

	@UiHandler("frmForm")
	void onFormSubmit (SubmitEvent se) {
		if (isValid()) {
			loading();

			Form form = new Form();

			// add fields to form

			FormController.get().submitForm(form);
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

			boolean autofocus = pnlFields.getWidgetCount() == 0;

			switch (config.type) {
			case TEXT_TYPE:
				TextBoxPart textBox = new TextBoxPart();
				textBox.elName.setInnerHTML(config.name);
				UiHelper.addPlaceholder(textBox.txtValue, config.name);
				textBox.txtValue.setValue(config.defaultValue);
				pnlFields.add(textBox);
				if (autofocus) {
					UiHelper.autoFocus(textBox.txtValue);
				}
				break;
			case LONG_TEXT_TYPE:
				TextAreaPart longText = new TextAreaPart();
				longText.elName.setInnerHTML(config.name);
				UiHelper.addPlaceholder(longText.txtValue, config.name);
				longText.txtValue.setValue(config.defaultValue);
				longText.txtValue.setVisibleLines(4);
				pnlFields.add(longText);
				if (autofocus) {
					UiHelper.autoFocus(longText.txtValue);
				}
				break;
			case ONE_OPTION_TYPE:
				ListBoxPart listBox = new ListBoxPart();
				listBox.elName.setInnerHTML(config.name);
				UiHelper.addPlaceholder(listBox.lbxValue, config.name);

				if (config.allowedValues != null) {
					int size = config.allowedValues.size();
					String value;
					for (int i = 0; i < size; i++) {
						value = config.allowedValues.get(i);
						listBox.lbxValue.addItem(value, value);
						if (value.equals(config.defaultValue)) {
							listBox.lbxValue.setSelectedIndex(i);
						}
					}
				}
				pnlFields.add(listBox);
				if (autofocus) {
					UiHelper.autoFocus(listBox.lbxValue);
				}
				break;
			case CAPTCHA_TYPE:
				ReCaptchaPart recaptch = new ReCaptchaPart();
				recaptch.setApiKey(config.parameters.get(RECAPTCH_API_KEY));
				pnlFields.add(recaptch);
				break;
			}
		}
	}

	private ConfigLine parseConfigLine (String line) {
		String[] params = line.split("/");
		ConfigLine config = new ConfigLine();

		String[] splitParam, allowedValues;
		Map<String, String> parameters = new HashMap<String, String>();
		for (String param : params) {
			splitParam = param.split("=");

			if (splitParam.length == 2) {
				parameters.put(splitParam[0], splitParam[1]);

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

		config.parameters = parameters;

		return config;
	}

	/**
	 * @param params
	 */
	public void setParams (Map<String, String> params) {
		String[] splitParam;
		if (params.containsKey(FORM_CLASS_PARAM_KEY)) {
			splitParam = params.get(FORM_CLASS_PARAM_KEY).split(";");
			for (String token : splitParam) {
				this.addStyleName(token);
			}
		}

		if (params.containsKey(BODY_PANEL_CLASS_PARAM_KEY)) {
			splitParam = params.get(BODY_PANEL_CLASS_PARAM_KEY).split(";");
			for (String token : splitParam) {
				pnlBody.addStyleName(token);
			}
		}

		if (params.containsKey(FIELD_PANEL_CLASS_PARAM_KEY)) {
			splitParam = params.get(FIELD_PANEL_CLASS_PARAM_KEY).split(";");
			for (String token : splitParam) {
				pnlFields.addStyleName(token);
			}
		}

		if (params.containsKey(BUTTON_BAR_CLASS_PARAM_KEY)) {
			splitParam = params.get(BUTTON_BAR_CLASS_PARAM_KEY).split(";");
			for (String token : splitParam) {
				pnlButtons.addStyleName(token);
			}
		}
	}

	private void loading () {
		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Sending... ", Resources.RES.primaryLoader()
								.getSafeUri()));
	}

	private void ready () {
		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.nextButton("Send"));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.SubmitFormEventHandler
	 * #submitFormSuccess
	 * (com.willshex.blogwt.shared.api.page.call.SubmitFormRequest,
	 * com.willshex.blogwt.shared.api.page.call.SubmitFormResponse) */
	@Override
	public void submitFormSuccess (SubmitFormRequest input,
			SubmitFormResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			// show submission success message

			reset();
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.page.call.event.SubmitFormEventHandler
	 * #submitFormFailure
	 * (com.willshex.blogwt.shared.api.page.call.SubmitFormRequest,
	 * java.lang.Throwable) */
	@Override
	public void submitFormFailure (SubmitFormRequest input, Throwable caught) {
		ready();
	}

	private void reset () {
		frmForm.reset();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		registration = DefaultEventBus.get().addHandlerToSource(
				SubmitFormEventHandler.TYPE, FormController.get(), this);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		super.onDetach();

		if (registration != null) {
			registration.removeHandler();
		}
	}
}

//
//  FormPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 5 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.markdown.plugin.part;

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

/**
 * @author William Shakour (billy1380)
 *
 */
public class FormPart extends Composite {

	private static FormPartUiBinder uiBinder = GWT
			.create(FormPartUiBinder.class);

	interface FormPartUiBinder extends UiBinder<Widget, FormPart> {}

	@UiField ResetButton btnReset;
	@UiField SubmitButton btnSubmit;
	@UiField FormPanel frmForm;
	@UiField HTMLPanel pnlFields;
	@UiField HTMLPanel pnlInstrucations;

	public FormPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("btnSubmit")
	void onSubmitClicked (SubmitEvent se) {
		// Call api with fields

		se.cancel();
	}

	@UiHandler("btnReset")
	void onResetClicked (ClickEvent ce) {
		frmForm.reset();
	}

}

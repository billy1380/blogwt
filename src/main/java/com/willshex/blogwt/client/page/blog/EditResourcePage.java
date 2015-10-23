//
//  EditResourcePage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 18 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author billy1380
 *
 */
public class EditResourcePage extends Page implements
		NavigationChangedEventHandler {

	private static EditResourcePageUiBinder uiBinder = GWT
			.create(EditResourcePageUiBinder.class);

	interface EditResourcePageUiBinder extends
			UiBinder<Widget, EditResourcePage> {}

	@UiField Element elHeading;
	@UiField FormPanel frmDetails;
	@UiField HTMLPanel pnlResource;
	@UiField HTMLPanel pnlName;
	@UiField TextBox txtName;
	@UiField HTMLPanel pnlNameNote;
	@UiField HTMLPanel pnlData;
	@UiField TextBox txtData;
	@UiField HTMLPanel pnlDescription;
	@UiField TextArea txtDescription;
	@UiField HTMLPanel pnlDescriptionNote;
	@UiField HTMLPanel pnlType;
	@UiField TextBox txtType;
	@UiField HTMLPanel pnlTypeNote;
	@UiField HTMLPanel pnlProperties;
	@UiField TextArea txtProperties;
	@UiField HTMLPanel pnlPropertiesNote;

	public EditResourcePage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void navigationChanged (Stack previous, Stack current) {

	}

	@Override
	protected void reset () {

		super.reset();
	}

}

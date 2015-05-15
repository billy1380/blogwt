//
//  EditPostPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPostPage extends Page {

	private static EditPostPageUiBinder uiBinder = GWT
			.create(EditPostPageUiBinder.class);

	interface EditPostPageUiBinder extends UiBinder<Widget, EditPostPage> {}

	@UiField HTMLPanel pnlTitle;
	@UiField TextBox txtTitle;
	@UiField HTMLPanel pnlTitleNote;
	@UiField HTMLPanel pnlSummary;
	@UiField TextArea txtSummary;
	@UiField HTMLPanel pnlContent;
	@UiField TextArea txtContent;
	@UiField HTMLPanel pnlTags;
	@UiField TextBox txtTags;
	@UiField HTMLPanel pnlTagsNote;
	@UiField CheckBox cbxDirectOnly;
	@UiField CheckBox cbxComments;
	@UiField CheckBox cbxPublish;
	@UiField SubmitButton btnSubmit;

	public EditPostPage () {
		super(PageType.EditPostPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtTitle, "Title");
		UiHelper.addPlaceholder(txtSummary, "Short Summary");
		UiHelper.addPlaceholder(txtContent, "Article Content");
		UiHelper.addPlaceholder(txtTags, "Comma Separated Tags");

		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Submit"));
	}
}

//
//  EditPostPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler;
import com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPostPage extends Page implements CreatePostEventHandler,
		UpdatePostEventHandler {

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

	@UiField HTMLPanel pnlPreview;

	public EditPostPage () {
		super(PageType.EditPostPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtTitle, "Title");
		UiHelper.addPlaceholder(txtSummary, "Short Summary");
		UiHelper.addPlaceholder(txtContent, "Article Content");
		UiHelper.addPlaceholder(txtTags, "Comma Separated Tags");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				CreatePostEventHandler.TYPE, PostController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdatePostEventHandler.TYPE, PostController.get(), this));

		ready();
	}

	@UiHandler({ "txtTitle", "txtSummary", "txtContent", "txtTags" })
	void onTxtKeyUp (KeyUpEvent e) {
		pnlPreview.getElement().removeAllChildren();

		Document d = Document.get();
		HeadingElement title = d.createHElement(1);
		title.setInnerText(txtTitle.getText());
		pnlPreview.getElement().appendChild(title);

		DivElement author = d.createDivElement();
		author.setInnerText(UserHelper.handle(SessionController.get().user()));
		pnlPreview.getElement().appendChild(author);

		DivElement on = d.createDivElement();
		on.setInnerText(DateTimeHelper.ago(PropertyController.get().started()));
		pnlPreview.getElement().appendChild(on);

		DivElement tags = d.createDivElement();
		tags.getStyle().setColor("green");
		tags.setInnerText(txtTags.getText());
		pnlPreview.getElement().appendChild(tags);

		ParagraphElement summary = d.createPElement();
		summary.addClassName("text-muted");
		summary.addClassName("text-justified");
		summary.setInnerText(txtSummary.getText());
		pnlPreview.getElement().appendChild(summary);

		ParagraphElement content = d.createPElement();
		content.addClassName("text-justified");
		content.setInnerText(txtContent.getText());
		pnlPreview.getElement().appendChild(content);
	}

	@UiHandler("btnSubmit")
	void onBtnSubmitClicked (ClickEvent e) {
		if (isValid()) {
			loading();
		} else {
			showErrors();
		}
	}

	private void ready () {
		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Submit"));

		txtTitle.setEnabled(true);
		txtSummary.setEnabled(true);
		txtContent.setEnabled(true);
		txtTags.setEnabled(true);
		cbxDirectOnly.setEnabled(true);
		cbxComments.setEnabled(true);
		cbxPublish.setEnabled(true);
		btnSubmit.setEnabled(true);
	}

	private void loading () {
		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Submitting... ", Resources.RES.primaryLoader()
								.getSafeUri()));

		txtTitle.setEnabled(false);
		txtSummary.setEnabled(false);
		txtContent.setEnabled(false);
		txtTags.setEnabled(false);
		cbxDirectOnly.setEnabled(false);
		cbxComments.setEnabled(false);
		cbxPublish.setEnabled(false);
		btnSubmit.setEnabled(false);

		pnlTitle.removeStyleName("has-error");
		pnlTitleNote.setVisible(false);
		pnlSummary.removeStyleName("has-error");
		pnlContent.removeStyleName("has-error");
		pnlTags.removeStyleName("has-error");
		pnlTagsNote.setVisible(false);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler
	 * #updatePostSuccess
	 * (com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest,
	 * com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse) */
	@Override
	public void updatePostSuccess (UpdatePostRequest input,
			UpdatePostResponse output) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler
	 * #updatePostFailure
	 * (com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest,
	 * java.lang.Throwable) */
	@Override
	public void updatePostFailure (UpdatePostRequest input, Throwable caught) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler
	 * #createPostSuccess
	 * (com.willshex.blogwt.shared.api.blog.call.CreatePostRequest,
	 * com.willshex.blogwt.shared.api.blog.call.CreatePostResponse) */
	@Override
	public void createPostSuccess (CreatePostRequest input,
			CreatePostResponse output) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler
	 * #createPostFailure
	 * (com.willshex.blogwt.shared.api.blog.call.CreatePostRequest,
	 * java.lang.Throwable) */
	@Override
	public void createPostFailure (CreatePostRequest input, Throwable caught) {
		// TODO Auto-generated method stub
	}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}
}

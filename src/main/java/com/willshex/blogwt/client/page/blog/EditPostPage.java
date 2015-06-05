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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.NavigationController.Stack;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler;
import com.willshex.blogwt.shared.api.blog.call.event.GetPostEventHandler;
import com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPostPage extends Page implements
		NavigationChangedEventHandler, CreatePostEventHandler,
		GetPostEventHandler, UpdatePostEventHandler {

	private static EditPostPageUiBinder uiBinder = GWT
			.create(EditPostPageUiBinder.class);

	interface EditPostPageUiBinder extends UiBinder<Widget, EditPostPage> {}

	private Post post;

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

	private Timer updateTimer = new Timer() {
		@Override
		public void run () {
			updatePreview();
		}
	};;

	public EditPostPage () {
		super(PageType.EditPostPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtTitle, "Title");
		UiHelper.autoFocus(txtTitle);
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
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				CreatePostEventHandler.TYPE, PostController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdatePostEventHandler.TYPE, PostController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetPostEventHandler.TYPE, PostController.get(), this));

		post = null;

		updateTimer.cancel();
		updateTimer.schedule(250);

		ready();
	}

	@UiHandler({ "txtTitle", "txtSummary", "txtContent", "txtTags" })
	void onTxtKeyUp (KeyUpEvent e) {
		updateTimer.cancel();
		updateTimer.schedule(250);
	}

	private void updatePreview () {
		pnlPreview.getElement().removeAllChildren();

		Document d = Document.get();
		HeadingElement title = d.createHElement(1);

		title.setInnerHTML(PostHelper.makeHeading(txtTitle.getText()));
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

		DivElement summary = d.createDivElement();
		String summaryMarkdown = PostHelper.makeMarkup(txtSummary.getText());
		summary.setInnerHTML(summaryMarkdown);
		pnlPreview.getElement().appendChild(summary);

		DivElement content = d.createDivElement();
		String contextMarkdown = PostHelper.makeMarkup(txtContent.getText());
		content.setInnerHTML(contextMarkdown);
		pnlPreview.getElement().appendChild(content);
	}

	@UiHandler("btnSubmit")
	void onBtnSubmitClicked (ClickEvent e) {
		if (isValid()) {
			if (post == null) {
				PostController.get().createPost(
						txtTitle.getText(),
						cbxDirectOnly.getValue().booleanValue() ? Boolean.FALSE
								: Boolean.TRUE, cbxComments.getValue(),
						txtSummary.getText(), txtContent.getText(),
						cbxPublish.getValue(), txtTags.getText());
			} else {
				PostController.get().updatePost(
						post,
						txtTitle.getText(),
						cbxDirectOnly.getValue().booleanValue() ? Boolean.FALSE
								: Boolean.TRUE, cbxComments.getValue(),
						txtSummary.getText(), txtContent.getText(),
						cbxPublish.getValue(), txtTags.getText());
			}
			submitting();
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

	private void submitting () {
		loading();

		btnSubmit.getElement().setInnerSafeHtml(
				WizardDialog.WizardDialogTemplates.INSTANCE.loadingButton(
						"Submitting... ", Resources.RES.primaryLoader()
								.getSafeUri()));
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
		if (output.status == StatusType.StatusTypeFailure) {

		} else {
			PageType.PostDetailPageType.show(PostHelper
					.slugify(input.post.title));
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.UpdatePostEventHandler
	 * #updatePostFailure
	 * (com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest,
	 * java.lang.Throwable) */
	@Override
	public void updatePostFailure (UpdatePostRequest input, Throwable caught) {}

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
		if (output.status == StatusType.StatusTypeFailure) {

		} else {
			PageType.PostDetailPageType.show(PostHelper
					.slugify(input.post.title));
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.CreatePostEventHandler
	 * #createPostFailure
	 * (com.willshex.blogwt.shared.api.blog.call.CreatePostRequest,
	 * java.lang.Throwable) */
	@Override
	public void createPostFailure (CreatePostRequest input, Throwable caught) {}

	private boolean isValid () {
		// do client validation
		return true;
	}

	private void showErrors () {

	}

	private void show (Post post) {
		txtTitle.setText(post.title);

		StringBuffer tags = new StringBuffer();
		for (String tag : post.tags) {
			if (tags.length() > 0) {
				tags.append(", ");
			}

			tags.append(tag);
		}

		txtTags.setText(tags.toString());

		txtSummary.setText(post.summary);
		txtContent.setText(post.content.body);

		cbxDirectOnly.setValue(Boolean.valueOf(post.listed != null
				&& !post.listed.booleanValue()));
		cbxComments.setValue(post.commentsEnabled == null ? Boolean.FALSE
				: post.commentsEnabled);
		cbxPublish.setValue(Boolean.valueOf(post.published != null));

		updatePreview();

		ready();
	}

	@Override
	public void getPostSuccess (GetPostRequest input, GetPostResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(post = output.post);
		} else {
			PageType.PostsPageType.show();
		}
	}

	@Override
	public void getPostFailure (GetPostRequest input, Throwable caught) {}

	@Override
	public void navigationChanged (Stack previous, Stack current) {
		if (current.getAction() != null && !"new".equals(current.getAction())) {
			String postParam;
			if ((postParam = current.getAction()) != null) {
				PostController.get().getPost(postParam);
				loading();
			}
		}
	}
}

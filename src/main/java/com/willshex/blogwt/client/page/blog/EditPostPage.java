//
//  EditPostPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
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
import com.willshex.blogwt.client.part.MarkdownToolbar;
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
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceTypeType;
import com.willshex.blogwt.shared.api.datatype.User;
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
	@UiField Element elHeading;
	@UiField HTMLPanel pnlPreview;
	@UiField HTMLPanel pnlLoading;
	@UiField FormPanel frmEdit;
	@UiField MultiUploader uplDragAndDrop;
	@UiField HTMLPanel pnlImagePreviews;
	@UiField MarkdownToolbar tbrSummary;
	@UiField MarkdownToolbar tbrContent;
	private Map<String, Resource> resources;
	private HTMLPanel currentResourceRow;

	private static final int IMAGES_PER_ROW = 4;
	private static final String CARET_BEFORE = " \\_\\:\\_this\\_is\\_the\\_tracking\\_cursor\\_\\:\\_ ";
	private static final String CARET_AFTER = "_:_this_is_the_tracking_cursor_:_";
	// private static final String CARET = " <span class=\"glyphicon glyphicon-pencil\"></span> ";
	private static final String CARET_FINALLY = "<span class=\""
			+ Resources.RES.styles().blink()
			+ "\"><strong> &#9724; </strong></span>";

	private final OnLoadPreloadedImageHandler PRELOAD_HANDLER = new OnLoadPreloadedImageHandler() {

		@Override
		public void onLoad (PreloadedImage image) {
			image.addStyleName("img-circle");
			image.addStyleName("col-xs-" + (int) (12 / IMAGES_PER_ROW));

			if ((resources.size() - 1) % IMAGES_PER_ROW == 0) {
				currentResourceRow = new HTMLPanel(
						SafeHtmlUtils.EMPTY_SAFE_HTML);
				currentResourceRow.addStyleName("row");
				pnlImagePreviews.add(currentResourceRow);
			}

			currentResourceRow.add(image);
		}
	};

	private Timer updateTimer = new Timer() {
		@Override
		public void run () {
			updatePreview();
		}
	};

	public EditPostPage () {
		super(PageType.EditPostPageType);
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtTitle, "Title");
		UiHelper.autoFocus(txtTitle);
		UiHelper.addPlaceholder(txtSummary, "Short Summary");
		UiHelper.addPlaceholder(txtContent, "Article Content");
		UiHelper.addPlaceholder(txtTags, "Comma Separated Tags");

		uplDragAndDrop.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish (IUploader uploader) {
				if (uploader.getStatus() == Status.SUCCESS) {
					String msg = uploader.getServerMessage().getMessage();
					if (msg != null && msg.startsWith("data:")) {
						// NOTE: this does not happen
						new PreloadedImage(msg, PRELOAD_HANDLER);
					} else {
						Resource resource = new Resource();
						resource.type = ResourceTypeType.ResourceTypeTypeBlobStoreImage;

						for (String url : uploader.getServerMessage()
								.getUploadedFileUrls()) {
							resource.data = url;
							break;
						}

						for (String name : uploader.getServerMessage()
								.getUploadedFileNames()) {
							resource.name = name;
							break;
						}

						ensureResources().put(resource.name, resource);
						uploader.getStatusWidget().setVisible(false);
						new PreloadedImage(resource.data, PRELOAD_HANDLER);
					}
				} else {
					// Failed :(
				}
			}
		});
		uplDragAndDrop.setStatusWidget(new BaseUploadStatus());
		tbrSummary.setText(txtSummary);
		tbrContent.setText(txtContent);

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

		register(PostHelper.handlePluginContentReady());

		post = null;

		deferRefresh();

		ready();
	}

	/**
	 * 
	 */
	private void deferRefresh () {
		updateTimer.cancel();
		updateTimer.schedule(250);
	}

	@UiHandler({ "txtTitle", "txtSummary", "txtContent", "txtTags" })
	void onTxtKeyUp (KeyUpEvent e) {
		deferRefresh();
	}

	@UiHandler({ "txtSummary", "txtContent" })
	void onClick (ClickEvent e) {
		deferRefresh();
	}

	private void updatePreview () {
		pnlPreview.getElement().removeAllChildren();

		Document d = Document.get();
		HeadingElement title = d.createHElement(1);

		title.setInnerHTML(PostHelper.makeHeading(txtTitle.getText()));
		pnlPreview.getElement().appendChild(title);

		User user = SessionController.get().user();
		DivElement author = d.createDivElement();
		author.setInnerHTML("By <img src=\"" + user.avatar + "?s="
				+ UserHelper.AVATAR_SIZE
				+ "&default=retro\" class=\"img-circle\" > "
				+ UserHelper.handle(user));
		pnlPreview.getElement().appendChild(author);

		DivElement on = d.createDivElement();
		on.setInnerText(DateTimeHelper.ago(PropertyController.get().started()));
		pnlPreview.getElement().appendChild(on);

		DivElement tags = d.createDivElement();
		tags.getStyle().setColor("green");
		tags.setInnerText(txtTags.getText());
		pnlPreview.getElement().appendChild(tags);

		DivElement summary = d.createDivElement();
		summary.setInnerHTML(markup(txtSummary));
		pnlPreview.getElement().appendChild(summary);

		DivElement content = d.createDivElement();
		content.setInnerHTML(markup(txtContent));
		pnlPreview.getElement().appendChild(content);
	}

	/**
	 * @param text
	 * @return
	 */
	private String markup (ValueBoxBase<String> valueBox) {
		StringBuffer markdown = new StringBuffer(valueBox.getText());
		markdown.insert(valueBox.getCursorPos(), CARET_BEFORE);
		return PostHelper.makeMarkup(markdown.toString())
				.replace(CARET_BEFORE, CARET_FINALLY)
				.replace(CARET_AFTER, CARET_FINALLY);
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

		pnlLoading.setVisible(false);
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

		pnlLoading.setVisible(true);
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

		if (post.tags != null) {
			StringBuffer tags = new StringBuffer();
			for (String tag : post.tags) {
				if (tags.length() > 0) {
					tags.append(", ");
				}

				tags.append(tag);
			}

			txtTags.setText(tags.toString());
		}

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
		boolean isNewPost = true;

		if (current.getAction() != null && !"new".equals(current.getAction())) {
			String postParam;
			if ((postParam = current.getAction()) != null) {
				PostController.get().getPost(postParam);
				loading();
				isNewPost = false;
			}
		}
		elHeading.setInnerText(isNewPost ? "New Post" : "Edit Post");
	}

	private Map<String, Resource> ensureResources () {
		return resources == null ? resources = new HashMap<String, Resource>()
				: resources;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();

		frmEdit.reset();
		cbxDirectOnly.setValue(Boolean.FALSE);
		cbxComments.setValue(Boolean.FALSE);
		cbxPublish.setValue(Boolean.FALSE);

		if (resources != null) {
			resources.clear();
		}

		// TODO: hide error messages etc

		updatePreview();
	}
}

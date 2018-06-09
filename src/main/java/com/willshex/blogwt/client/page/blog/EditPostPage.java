//
//  EditPostPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.blog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.api.blog.event.CreatePostEventHandler;
import com.willshex.blogwt.client.api.blog.event.GetPostEventHandler;
import com.willshex.blogwt.client.api.blog.event.UpdatePostEventHandler;
import com.willshex.blogwt.client.cell.blog.PostSummaryCell;
import com.willshex.blogwt.client.cell.blog.TagCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PostController;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.PostHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.InlineBootstrapGwtCellList;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.MarkdownToolbar;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceTypeType;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.TagHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

/**
 * @author William Shakour (billy1380)
 *
 */
public class EditPostPage extends Page implements CreatePostEventHandler,
		GetPostEventHandler, UpdatePostEventHandler {

	private static EditPostPageUiBinder uiBinder = GWT
			.create(EditPostPageUiBinder.class);

	interface EditPostPageUiBinder extends UiBinder<Widget, EditPostPage> {}

	//	interface Style extends CssResource {
	//		@ClassName("drop-zone")
	//		String dropZone ();
	//	}

	private static final int IMAGES_PER_ROW = 4;
	private static final String CARET_BEFORE = " \\_\\:\\_this\\_is\\_the\\_tracking\\_cursor\\_\\:\\_ ";
	private static final String CARET_AFTER = "_:_this_is_the_tracking_cursor_:_";
	// private static final String CARET = " <span class=\"glyphicon glyphicon-pencil\"></span> ";
	private static final String CARET_FINALLY = "<span class=\""
			+ Resources.RES.styles().blink()
			+ "\"><strong> &#9724; </strong></span>";
	private static final int SAVE_EVERY = 2
			* (int) DateTimeHelper.MILLIS_PER_MIN;

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
	@UiField HTMLPanel pnlComments;
	@UiField CheckBox cbxComments;
	@UiField CheckBox cbxPublish;
	@UiField SubmitButton btnSave;
	@UiField SubmitButton btnSaveAndShow;
	@UiField Element elHeading;
	@UiField HTMLPanel pnlPreview;
	@UiField LoadingPanel pnlLoading;
	@UiField FormPanel frmEdit;
	//	@UiField(provided = true) Label lblDropZone = new Label(
	//			"Drag and Drop files here.");
	//	@UiField(provided = true) Button btnUploadImage = new Button(
	//			"Upload files...");
	@UiField(provided = true) MultiUploader uplDragAndDrop = new MultiUploader(
			FileInputType.BROWSER_INPUT
	//			.with(btnUploadImage).withZone(lblDropZone)
	);
	@UiField HTMLPanel pnlImagePreviews;
	@UiField MarkdownToolbar tbrSummary;
	@UiField MarkdownToolbar tbrContent;
	//	@UiField Style style;

	@UiField ToggleButton btnLiveUpdate;
	@UiField Button btnRefresh;

	@UiField(provided = true) CellList<Tag> clTags = new CellList<Tag>(
			new TagCell(true, false), InlineBootstrapGwtCellList.INSTANCE);

	private ListDataProvider<Tag> tagList = new ListDataProvider<Tag>();

	private Map<String, Resource> resources;
	private HTMLPanel currentResourceRow;
	private Focusable current;
	private boolean stop;
	private boolean saveAndShow = false;

	private final OnLoadPreloadedImageHandler PRELOAD_HANDLER = new OnLoadPreloadedImageHandler() {

		@Override
		public void onLoad (PreloadedImage image) {
			image.addStyleName("img-rounded");
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

	private final Timer updateTimer = new Timer() {
		@Override
		public void run () {
			updatePreview();
		}
	};

	private final Timer saveTimer = new Timer() {
		@Override
		public void run () {
			save(btnSave);
		}

	};
	private int changes;
	private int changesAtSave;

	public EditPostPage () {
		initWidget(uiBinder.createAndBindUi(this));
		//		lblDropZone.setStyleName(style.dropZone());
		//		btnUploadImage.setStyleName("btn");
		//		btnUploadImage.addStyleName("btn-default");
		UiHelper.addPlaceholder(txtTitle, "Title");
		UiHelper.autoFocus(txtTitle);
		UiHelper.addPlaceholder(txtSummary, "Short Summary");
		UiHelper.addPlaceholder(txtContent, "Article Content");
		UiHelper.addPlaceholder(txtTags, "Comma Separated Tags");
		uplDragAndDrop.setAutoSubmit(true);
		uplDragAndDrop.setAvoidRepeatFiles(true);
		uplDragAndDrop.setValidExtensions("jpg", "jpeg", "png");
		uplDragAndDrop.setServletPath(ApiHelper.UPLOAD_END_POINT);

		uplDragAndDrop.addOnFinishUploadHandler(
				EditPostPage.this::finishedImageUpload);
		uplDragAndDrop.setStatusWidget(new BaseUploadStatus());
		tbrSummary.setText(txtSummary);
		tbrContent.setText(txtContent);

		tagList.addDataDisplay(clTags);

		String commentsEnabled = PropertyController.get()
				.stringProperty(PropertyHelper.POST_COMMENTS_ENABLED);
		pnlComments.setVisible(commentsEnabled != null
				&& !PropertyHelper.NONE_VALUE.equals(commentsEnabled));

	}

	private void finishedImageUpload (IUploader uploader) {
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
					resource.data = url.replace(ApiHelper.BASE_URL, "/");
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

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					boolean isNewPost = true;

					if (c.getAction() != null && !"new".equals(c.getAction())) {
						String postParam;
						if ((postParam = c.getAction()) != null) {
							PostController.get().getPost(postParam);
							loading();
							isNewPost = false;
						}
					}

					elHeading
							.setInnerText(isNewPost ? "New Post" : "Edit Post");
				}));
		register(DefaultEventBus.get().addHandlerToSource(
				CreatePostEventHandler.TYPE, PostController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdatePostEventHandler.TYPE, PostController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				GetPostEventHandler.TYPE, PostController.get(), this));

		register(PostHelper.handlePluginContentReady());

		post = null;

		deferUpdate();

		ready();
	}

	private void deferUpdate () {
		if (!stop) {
			updateTimer.cancel();
			updateTimer.schedule(1000);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onDetach() */
	@Override
	protected void onDetach () {
		updateTimer.cancel();
		saveTimer.cancel();

		super.onDetach();
	}

	@UiHandler({ "txtTitle", "txtSummary", "txtContent", "txtTags" })
	void onTxtKeyUp (KeyUpEvent e) {
		deferUpdate();
		changes++;
	}

	@UiHandler({ "cbxDirectOnly", "cbxComments", "cbxPublish" })
	void onChecked (ValueChangeEvent<Boolean> vce) {
		changes++;
	}

	@UiHandler({ "txtTitle", "txtSummary", "txtContent", "txtTags" })
	void onClick (ClickEvent e) {
		deferUpdate();
		this.current = (Focusable) e.getSource();
	}

	@UiHandler({ "btnLiveUpdate" })
	void onLiveUpdateClicked (ClickEvent ce) {
		stop = btnLiveUpdate.isDown();

		btnRefresh.setVisible(stop);

		if (stop) {
			updateTimer.cancel();
		} else {
			updatePreview();
		}
	}

	@UiHandler({ "btnRefresh" })
	void onRefreshClicked (ClickEvent ce) {
		updatePreview();
	}

	private void updatePreview () {
		pnlPreview.getElement().removeAllChildren();

		Document d = Document.get();
		HeadingElement title = d.createHElement(1);

		title.setInnerHTML(PostHelper.makeHeading(txtTitle.getValue()));
		pnlPreview.getElement().appendChild(title);

		User user = SessionController.get().user();

		DivElement elDate = d.createDivElement();
		if (post != null) {
			if (post.published == null) {
				elDate.setInnerSafeHtml(PostSummaryCell.Templates.INSTANCE
						.notPublished(DateTimeHelper.ago(post.created)));
			} else {
				elDate.setInnerSafeHtml(PostSummaryCell.Templates.INSTANCE
						.publishedDate(DateTimeHelper.ago(post.published)));
			}
		} else {
			elDate.setInnerSafeHtml(PostSummaryCell.Templates.INSTANCE
					.notPublished(DateTimeHelper.ago(new Date())));
		}

		pnlPreview.getElement().appendChild(elDate);

		DivElement elAuthor = d.createDivElement();
		if (PropertyController.get()
				.booleanProperty(PropertyHelper.POST_SHOW_AUTHOR, false)) {
			elAuthor.setInnerSafeHtml(PostSummaryCell.Templates.INSTANCE.author(
					UriUtils.fromString(
							(post != null ? post.author.avatar : user.avatar)
									+ "?s=" + UserHelper.AVATAR_HEADER_SIZE
									+ "&default=retro"),
					UserHelper.handle((post != null ? post.author : user))));
		}

		pnlPreview.getElement().appendChild(elAuthor);

		tagList.getList().clear();
		List<String> tags = TagHelper.convertToTagList(txtTags.getValue());
		if (tags != null) {
			for (String tag : tags) {
				tagList.getList().add(new Tag().name(tag));
			}
		}

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
		StringBuffer markdown = new StringBuffer(valueBox.getValue());
		markdown.insert(valueBox.getCursorPos(), CARET_BEFORE);
		return PostHelper.makeMarkup(markdown.toString())
				.replace(CARET_BEFORE, CARET_FINALLY)
				.replace(CARET_AFTER, CARET_FINALLY);
	}

	@UiHandler({ "btnSaveAndShow", "btnSave" })
	void onBtnSubmitClicked (ClickEvent e) {
		save((SubmitButton) e.getSource());
	}

	private void save (SubmitButton source) {
		if (isValid()) {
			saveAndShow = (source == btnSaveAndShow);

			if (changes > 0) {
				changesAtSave = changes;

				if (post == null) {
					PostController.get().createPost(txtTitle.getValue(),
							cbxDirectOnly.getValue().booleanValue()
									? Boolean.FALSE
									: Boolean.TRUE,
							cbxComments.getValue(), txtSummary.getValue(),
							txtContent.getValue(), cbxPublish.getValue(),
							txtTags.getValue());
				} else {
					PostController.get().updatePost(post, txtTitle.getValue(),
							cbxDirectOnly.getValue().booleanValue()
									? Boolean.FALSE
									: Boolean.TRUE,
							cbxComments.getValue(), txtSummary.getValue(),
							txtContent.getValue(), cbxPublish.getValue(),
							txtTags.getValue());
				}

				submitting();
			} else {
				restoreFocus();

				if (saveAndShow) {
					PageTypeHelper.show(PageType.PostDetailPageType,
							PostHelper.slugify(txtTitle.getValue()));
				}
			}
		} else {
			showErrors();
		}
	}

	private void restoreFocus () {
		if (current == null) {
			current = txtTitle;
		}

		current.setFocus(true);
	}

	private void ready () {
		saveTimer.scheduleRepeating(SAVE_EVERY);

		btnSaveAndShow.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton("Submit"));

		txtTitle.setEnabled(true);
		txtSummary.setEnabled(true);
		txtContent.setEnabled(true);
		txtTags.setEnabled(true);
		cbxDirectOnly.setEnabled(true);
		cbxComments.setEnabled(true);
		cbxPublish.setEnabled(true);
		btnSaveAndShow.setEnabled(true);
		btnSave.setEnabled(true);

		pnlLoading.setVisible(false);

		restoreFocus();
	}

	private void loading () {
		saveTimer.cancel();

		txtTitle.setEnabled(false);
		txtSummary.setEnabled(false);
		txtContent.setEnabled(false);
		txtTags.setEnabled(false);
		cbxDirectOnly.setEnabled(false);
		cbxComments.setEnabled(false);
		cbxPublish.setEnabled(false);
		btnSaveAndShow.setEnabled(false);
		btnSave.setEnabled(false);

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

		btnSaveAndShow.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.loadingButton("Submitting... ",
								Resources.RES.primaryLoader().getSafeUri()));
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
			if (output.post != null) {
				post = output.post;
			}
		} else {
			if (saveAndShow) {
				PageTypeHelper.show(PageType.PostDetailPageType,
						PostHelper.slugify(input.post.title));
			}

			changes -= changesAtSave;
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
	public void updatePostFailure (UpdatePostRequest input, Throwable caught) {
		ready();
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
		if (output.status == StatusType.StatusTypeFailure) {
			if (output.post != null) {
				post = output.post;
			}
		} else {
			if (saveAndShow) {
				PageTypeHelper.show(PageType.PostDetailPageType,
						PostHelper.slugify(input.post.title));
			}

			changes -= changesAtSave;
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
	public void createPostFailure (CreatePostRequest input, Throwable caught) {
		ready();
	}

	private boolean isValid () {
		// do client validation
		return txtTitle.getValue().trim().length() > 0;
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

		cbxDirectOnly.setValue(Boolean
				.valueOf(post.listed != null && !post.listed.booleanValue()));
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
			NavigationController.get().lost();
		}
	}

	@Override
	public void getPostFailure (GetPostRequest input, Throwable caught) {
		ready();
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

		tagList.getList().clear();

		updatePreview();
	}
}

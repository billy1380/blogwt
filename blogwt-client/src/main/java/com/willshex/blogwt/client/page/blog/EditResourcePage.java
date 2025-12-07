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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.api.blog.event.GetResourceEventHandler;
import com.willshex.blogwt.client.api.blog.event.UpdateResourceEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.ResourceController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceResponse;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceTypeType;
import com.willshex.blogwt.shared.api.upload.Upload;
import com.willshex.gson.web.service.shared.StatusType;

import elemental2.dom.FormData;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.XMLHttpRequest;
import jsinterop.base.Js;

/**
 * @author billy1380
 *
 */
public class EditResourcePage extends Page
		implements GetResourceEventHandler, UpdateResourceEventHandler {

	private static EditResourcePageUiBinder uiBinder = GWT
			.create(EditResourcePageUiBinder.class);

	interface EditResourcePageUiBinder
			extends UiBinder<Widget, EditResourcePage> {
	}

	@UiField
	Element elHeading;
	@UiField
	FormPanel frmDetails;
	@UiField
	HTMLPanel pnlResource;
	@UiField
	HTMLPanel pnlResourcePreview;
	// for now this is not drag and drop
	@UiField
	FileUpload uplDragAndDrop;
	@UiField
	Label lblDropZone;
	@UiField
	HTMLPanel pnlName;
	@UiField
	TextBox txtName;
	@UiField
	HTMLPanel pnlNameNote;
	@UiField
	HTMLPanel pnlData;
	@UiField
	TextBox txtData;
	@UiField
	HTMLPanel pnlDescription;
	@UiField
	TextArea txtDescription;
	@UiField
	HTMLPanel pnlDescriptionNote;
	@UiField
	HTMLPanel pnlType;
	@UiField
	TextBox txtType;
	@UiField
	HTMLPanel pnlTypeNote;
	@UiField
	HTMLPanel pnlProperties;
	@UiField
	TextArea txtProperties;
	@UiField
	HTMLPanel pnlPropertiesNote;

	@UiField
	Button btnUpdate;

	private Resource resource;
	private String actionText;

	private static final String UPDATE_ACTION_TEXT = "Update";
	private static final String ADD_ACTION_TEXT = "Add";

	public EditResourcePage() {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtName, "Name");
		UiHelper.addPlaceholder(txtData, "Data");
		UiHelper.addPlaceholder(txtDescription, "Description");
		UiHelper.addPlaceholder(txtType, "Type");
		UiHelper.addPlaceholder(txtProperties, "Properties");

		uplDragAndDrop.getElement().setAttribute("accept",
				"jpg,jpeg,png".replaceAll(",", ",."));

		uplDragAndDrop.addChangeHandler(e -> {
			HTMLInputElement input = Js.cast(uplDragAndDrop.getElement());
			upload(input.files);
		});

		elemental2.dom.Element dropZone = Js.cast(lblDropZone.getElement());
		dropZone.addEventListener("click", e -> {
			Js.<HTMLInputElement>uncheckedCast(uplDragAndDrop.getElement()).click();
		});

		dropZone.addEventListener("dragover", e -> {
			e.preventDefault();
			e.stopPropagation();
		});

		dropZone.addEventListener("drop", e -> {
			e.preventDefault();
			e.stopPropagation();

			elemental2.dom.DragEvent dragEvent = Js.cast(e);
			upload(dragEvent.dataTransfer.files);
		});
	}

	private void upload(elemental2.dom.FileList files) {
		if (files.length > 0) {
			RequestBuilder b = new RequestBuilder(RequestBuilder.GET,
					ApiHelper.UPLOAD_END_POINT);
			try {
				b.sendRequest(null, new RequestCallback() {

					@Override
					public void onResponseReceived(Request request,
							Response response) {
						if (200 == response.getStatusCode()) {
							String url = response.getText();

							FormData formData = new FormData();
							formData.append("image", files.item(0));

							XMLHttpRequest xhr = new XMLHttpRequest();
							xhr.open("POST", url);
							xhr.onload = v -> {
								if (xhr.status == 200) {
									String json = xhr.responseText;
									if (json != null && !json.isEmpty()) {
										try {
											com.google.gwt.json.client.JSONArray array = com.google.gwt.json.client.JSONParser
													.parseStrict(json).isArray();

											if (array != null && array.size() > 0) {
												com.google.gwt.json.client.JSONObject jsonResource = array.get(0)
														.isObject();

												if (jsonResource.containsKey("id")) {
													resource = new Resource();
													resource.id = (long) jsonResource.get("id").isNumber()
															.doubleValue();
													if (jsonResource.containsKey("name")) {
														resource.name = jsonResource.get("name").isString()
																.stringValue();
													}
													if (jsonResource.containsKey("data")) {
														resource.data = jsonResource.get("data").isString()
																.stringValue();
													}
													if (jsonResource.containsKey("description")) {
														resource.description = jsonResource.get("description")
																.isString().stringValue();
													}

													if (resource != null) {
														onImageUploadFinished(resource);
													}
												}
											}

										} catch (Exception e1) {
											GWT.log("Error parsing upload response", e1);
										}
									}
								}
								// cleanup
								// input.value = "";
							};
							xhr.send(formData);
						}
					}

					@Override
					public void onError(Request request,
							Throwable exception) {
					}
				});
			} catch (RequestException e1) {
			}
		}
	}

	private void onImageUploadFinished(Resource uploadedResource) {
		if (uploadedResource != null) {
			if (EditResourcePage.this.resource == null) {
				EditResourcePage.this.resource = uploadedResource;
			} else {
				// find out if the page status is new then delete the preciously uploaded
				// resource
			}

			show(EditResourcePage.this.resource = uploadedResource);

			if (uploadedResource.type == ResourceTypeType.ResourceTypeTypeGoogleCloudServiceImage) {
				String url = uploadedResource.data.replace(ApiHelper.BASE_URL, "");
				onImagePreloaderFinished(url);
			}
		}

		actionText = UPDATE_ACTION_TEXT;
		elHeading.setInnerText(getHeadingText());

		ready();
	}

	public void onImagePreloaderFinished(String url) {
		Image image = new Image(url);
		image.addStyleName("img-rounded");
		image.addStyleName("img-responsive");
		image.addStyleName("center-block");
		pnlResourcePreview.add(image);
	}

	private void show(Resource resource) {
		show(resource, false);
	}

	private void show(Resource resource, boolean withImage) {
		if (resource != null) {
			txtName.setValue(resource.name);
			txtData.setValue(resource.data);
			txtDescription.setValue(resource.description);
			txtProperties.setValue(resource.properties);
			txtType.setValue(
					resource.type == null ? "" : resource.type.toString());
			lblDropZone.setVisible(false);
			uplDragAndDrop.setVisible(false);

			if (withImage) {
				Image image = new Image(Upload.PATH + "?blob-key="
						+ resource.data.replace("gs://", ""));
				image.addStyleName("img-rounded");
				image.addStyleName("img-responsive");
				image.addStyleName("center-block");
				pnlResourcePreview.add(image);
			}
		}
	}
	@Override
	protected void onAttach() {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					reset();

					if ("id".equals(c.getAction())
							&& c.getParameterCount() > 0) {
						Long id = Long.valueOf(c.getParameter(0));
						ResourceController.get().getResource(
								ApiHelper.dataType(new Resource(), id));
						actionText = UPDATE_ACTION_TEXT;
					} else if ("new".equals(c.getAction())) {
						actionText = ADD_ACTION_TEXT;
					}

					elHeading.setInnerText(getHeadingText());

					ready();
				}));
		register(DefaultEventBus.get().addHandlerToSource(
				GetResourceEventHandler.TYPE, ResourceController.get(), this));
		register(DefaultEventBus.get().addHandlerToSource(
				UpdateResourceEventHandler.TYPE, ResourceController.get(),
				this));
	}

	@Override
	protected void reset() {
		frmDetails.reset();

		actionText = UPDATE_ACTION_TEXT;
		resource = null;
		pnlResourcePreview.clear();

		uplDragAndDrop.setVisible(false);
		lblDropZone.setVisible(true);

		super.reset();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked(ClickEvent ce) {
		if (isValid()) {
			loading();

			resource.name(txtName.getValue())
					.description(txtDescription.getValue());

			ResourceController.get().updateResource(resource);
		} else {
			showErrors();
		}
	}

	private boolean isValid() {
		// do client validation
		return true;
	}

	private void ready() {
		btnUpdate.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.nextButton(actionText));

		btnUpdate.setEnabled(true);

		txtName.setFocus(true);
	}

	private void showErrors() {

	}

	private void loading() {
		btnUpdate.getElement()
				.setInnerSafeHtml(WizardDialog.WizardDialogTemplates.INSTANCE
						.loadingButton(getLoadingText(),
								Resources.RES.primaryLoader().getSafeUri()));

		btnUpdate.setEnabled(false);

		pnlName.removeStyleName("has-error");
		pnlNameNote.setVisible(false);
	}

	private String getLoadingText() {
		String loadingText = null;
		switch (actionText) {
			case UPDATE_ACTION_TEXT:
				loadingText = "Updating... ";
				break;
			case ADD_ACTION_TEXT:
				loadingText = "Adding... ";
				break;
		}

		return loadingText;
	}

	private String getHeadingText() {
		String headingText = null;
		switch (actionText) {
			case UPDATE_ACTION_TEXT:
				headingText = "Edit Resource";
				break;
			case ADD_ACTION_TEXT:
				headingText = "Add Resource";
				break;
		}

		return headingText;
	}
	@Override
	public void updateResourceSuccess(UpdateResourceRequest input,
			UpdateResourceResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(EditResourcePage.this.resource = output.resource, false);
		}

		ready();
	}
	@Override
	public void updateResourceFailure(UpdateResourceRequest input,
			Throwable caught) {
		GWT.log("updateResourceFailure", caught);
		ready();
	}
	@Override
	public void getResourceSuccess(GetResourceRequest input,
			GetResourceResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(EditResourcePage.this.resource = output.resource, true);
		}

		ready();
	}
	@Override
	public void getResourceFailure(GetResourceRequest input,
			Throwable caught) {
		GWT.log("getResourceFailure", caught);
		ready();
	}

}

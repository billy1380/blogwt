//
//  ImagePropertyPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 31 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.property;

import java.util.HashMap;
import java.util.Map;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceTypeType;

/**
 * @author billy1380
 *
 */
public class ImagePropertyPart extends AbstractPropertyPart {

	private static ImagePropertyPartUiBinder uiBinder = GWT
			.create(ImagePropertyPartUiBinder.class);

	interface ImagePropertyPartUiBinder extends
			UiBinder<Widget, ImagePropertyPart> {}

	@UiField Element elDescription;
	@UiField Element elName;
	@UiField TextBox txtValue;
	@UiField SingleUploader uplDragAndDrop;
	@UiField HTMLPanel pnlValueNote;
	@UiField HTMLPanel pnlImagePreviews;
	@UiField Button btnClear;

	private Map<String, Resource> resources;
	private HTMLPanel currentResourceRow;

	private static final int IMAGES_PER_ROW = 6;
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

	public ImagePropertyPart () {
		initWidget(uiBinder.createAndBindUi(this));

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
							resource.data = url
									.replace(ApiHelper.BASE_URL, "/");
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
						setValue(resource.data, true);
					}
				} else {
					// Failed :(
				}
			}
		});
		uplDragAndDrop.setStatusWidget(new BaseUploadStatus());

		btnClear.getElement().setInnerHTML(
				"<span class=\"glyphicon glyphicon-remove\"><span>");
	}

	private Map<String, Resource> ensureResources () {
		return resources == null ? resources = new HashMap<String, Resource>()
				: resources;
	}

	/**
	 * @param description
	 */
	public void setDescription (String description) {
		elDescription.setInnerText(description);
		UiHelper.addPlaceholder(txtValue, description);
	}

	/**
	 */
	public void setAutofocus () {
		UiHelper.autoFocus(txtValue);
	}

	@UiHandler("txtValue")
	void onTextValueChanged (ValueChangeEvent<String> vce) {
		setValue(vce.getValue(), true);
	}

	@UiHandler("btnClear")
	void onBtnClear (ClickEvent ce) {
		setValue("", true);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object,
	 * boolean) */
	@Override
	public void setValue (String value, boolean fireEvents) {
		if (value == null) {
			value = "";
		}

		String oldValue = getValue();

		txtValue.setValue(value);
		this.value = value;

		if (value.equals(oldValue)) { return; }
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasName#getName() */
	@Override
	public String getName () {
		return elName.getInnerText();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String) */
	@Override
	public void setName (String name) {
		elName.setInnerText(name);
	}

}

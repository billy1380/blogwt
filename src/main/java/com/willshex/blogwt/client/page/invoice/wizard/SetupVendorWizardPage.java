//
//  SetupVendorWizardPage.java
//  reckoning
//
//  Created by William Shakour (billy1380) on 26 Apr 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.ResourceTypeType;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;

import gwtupload.client.BaseUploadStatus;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SetupVendorWizardPage extends Composite
		implements WizardPage<Vendor> {

	private static SetupVendorWizardPageUiBinder uiBinder = GWT
			.create(SetupVendorWizardPageUiBinder.class);

	interface SetupVendorWizardPageUiBinder
			extends UiBinder<Widget, SetupVendorWizardPage> {}

	@UiField public TextBox txtName;
	@UiField public TextBox txtCode;
	@UiField public TextArea txtDescription;
	@UiField public TextBox txtEmail;
	@UiField public TextBox txtPhone;
	@UiField public Image imgLogo;
	public Image imgUploadLogo = null;
	@UiField public HTMLPanel pnlLogo;
	@UiField(provided = true) public SingleUploader uplDragAndDrop = new SingleUploader(
			FileInputType.DROPZONE);
	private Resource logoResource = null;

	private final OnLoadPreloadedImageHandler PRELOAD_HANDLER = new OnLoadPreloadedImageHandler() {

		@Override
		public void onLoad (PreloadedImage image) {
			imgLogo.setVisible(false);

			if (imgUploadLogo != null) {
				imgUploadLogo.removeFromParent();
			}

			imgUploadLogo = image;
			imgUploadLogo
					.setHeight(Integer.toString(imgLogo.getHeight()) + "px");
			imgUploadLogo.getElement().getStyle().setHeight(imgLogo.getHeight(),
					Unit.PX);
			imgUploadLogo.setStyleName(imgLogo.getStyleName());
			pnlLogo.add(imgUploadLogo);
		}
	};

	public SetupVendorWizardPage () {
		initWidget(uiBinder.createAndBindUi(this));

		txtName.getElement().setAttribute("placeholder", "Name");
		txtCode.getElement().setAttribute("placeholder", "Code");
		txtDescription.getElement().setAttribute("placeholder", "Description");
		txtEmail.getElement().setAttribute("placeholder", "Email");
		txtPhone.getElement().setAttribute("placeholder", "Phone");

		uplDragAndDrop.addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish (IUploader uploader) {
				if (uploader.getStatus() == Status.SUCCESS) {
					String msg = uploader.getServerMessage().getMessage();
					if (msg != null && msg.startsWith("data:")) {
						// NOTE: this does not happen
						new PreloadedImage(msg, PRELOAD_HANDLER);
					} else {
						logoResource = new Resource();
						logoResource.type = ResourceTypeType.ResourceTypeTypeBlobStoreImage;

						for (String url : uploader.getServerMessage()
								.getUploadedFileUrls()) {
							logoResource.data = url;
							break;
						}

						for (String name : uploader.getServerMessage()
								.getUploadedFileNames()) {
							logoResource.name = name;
							break;
						}

						// only preload one (many are not supported)
						new PreloadedImage(logoResource.data, PRELOAD_HANDLER);
					}
				} else {
					// Failed :(
				}
			}
		});
		uplDragAndDrop.setStatusWidget(new BaseUploadStatus());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#isRepeatable() */
	@Override
	public boolean isRepeatable () {
		return false;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getData() */
	@Override
	public Vendor getData () {
		Vendor vendor = new Vendor();

		vendor.name = txtName.getValue();
		vendor.code = txtCode.getValue();
		vendor.description = txtDescription.getValue();
		vendor.email = txtEmail.getValue();
		vendor.phone = txtPhone.getValue();
		vendor.logo = logoResource;

		if (vendor.logo != null) {
			vendor.logo.description = vendor.name + "'s logo";
		}

		return vendor;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getPageTitle() */
	@Override
	public String getPageTitle () {
		return "Details";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#getBody() */
	@Override
	public Widget getBody () {
		return this;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.quickinvoice.client.wizard.WizardPage#another() */
	@Override
	public WizardPage<?> another () {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.wizard.WizardPage#isValid() */
	@Override
	public boolean isValid () {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#setData(java.lang.Object)
	 */
	@Override
	public void setData (Vendor data) {}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getPageDescription()
	 */
	@Override
	public String getPageDescription () {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.willshex.blogwt.client.wizard.WizardPage#getAutoFocusField()
	 */
	@Override
	public Focusable getAutoFocusField () {
		return null;
	}

}

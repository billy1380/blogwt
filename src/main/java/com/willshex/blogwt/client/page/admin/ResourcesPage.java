//
//  ResourcesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.blog.ResourcePreviewCell;
import com.willshex.blogwt.client.controller.ResourceController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.api.blog.call.event.DeleteResourceEventHandler;
import com.willshex.blogwt.shared.api.blog.call.event.GetResourcesEventHandler;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourcesPage extends Page implements GetResourcesEventHandler,
		DeleteResourceEventHandler {

	private static ResourcesPageUiBinder uiBinder = GWT
			.create(ResourcesPageUiBinder.class);

	interface ResourcesPageUiBinder extends UiBinder<Widget, ResourcesPage> {}

	@UiField(provided = true) CellList<Resource> clResources = new CellList<Resource>(
			new ResourcePreviewCell(), BootstrapGwtCellList.INSTANCE);

	@UiField SimplePager pgrResources;
	@UiField NoneFoundPanel pnlNoResources;

	public ResourcesPage () {
		initWidget(uiBinder.createAndBindUi(this));

		clResources.setEmptyListWidget(pnlNoResources);
		ResourceController.get().addDataDisplay(clResources);
		pgrResources.setDisplay(clResources);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.page.Page#register(com.google.gwt.event.shared
	 * .HandlerRegistration) */
	@Override
	protected void register (HandlerRegistration registration) {
		super.register(registration);

		register(DefaultEventBus.get().addHandlerToSource(
				GetResourcesEventHandler.TYPE, ResourceController.get(), this));
		register(DefaultEventBus.get()
				.addHandlerToSource(DeleteResourceEventHandler.TYPE,
						ResourceController.get(), this));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.DeleteResourceEventHandler
	 * #deleteResourceSuccess(com.willshex.blogwt.shared.api.blog.call.
	 * DeleteResourceRequest,
	 * com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse) */
	@Override
	public void deleteResourceSuccess (DeleteResourceRequest input,
			DeleteResourceResponse output) {}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.DeleteResourceEventHandler
	 * #deleteResourceFailure(com.willshex.blogwt.shared.api.blog.call.
	 * DeleteResourceRequest, java.lang.Throwable) */
	@Override
	public void deleteResourceFailure (DeleteResourceRequest input,
			Throwable caught) {

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.GetResourcesEventHandler
	 * #getResourcesSuccess
	 * (com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest,
	 * com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse) */
	@Override
	public void getResourcesSuccess (GetResourcesRequest input,
			GetResourcesResponse output) {

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.GetResourcesEventHandler
	 * #getResourcesFailure
	 * (com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest,
	 * java.lang.Throwable) */
	@Override
	public void getResourcesFailure (GetResourcesRequest input, Throwable caught) {

	}

}

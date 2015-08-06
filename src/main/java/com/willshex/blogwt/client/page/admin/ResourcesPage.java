//
//  ResourcesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.ResourceController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.helper.PagerHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourcesPage extends Page {

	private static ResourcesPageUiBinder uiBinder = GWT
			.create(ResourcesPageUiBinder.class);

	interface ResourcesPageUiBinder extends UiBinder<Widget, ResourcesPage> {}

	@UiField(provided = true) CellTable<Resource> tblResources = new CellTable<Resource>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);

	@UiField SimplePager pgrResources;
	@UiField NoneFoundPanel pnlNoResources;
	
//	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();
//	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

	public ResourcesPage () {
		super(PageType.ResourcesPageType);
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblResources.setEmptyTableWidget(pnlNoResources);
		ResourceController.get().addDataDisplay(tblResources);
		pgrResources.setDisplay(tblResources);
	}

	private void createColumns () {

	}

}

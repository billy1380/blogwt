//
//  PermissionsPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.PermissionController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.helper.PagerHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionsPage extends Page {

	private static PermissionsPageUiBinder uiBinder = GWT
			.create(PermissionsPageUiBinder.class);

	interface PermissionsPageUiBinder extends UiBinder<Widget, PermissionsPage> {}

	@UiField(provided = true) CellTable<Permission> tblPermissions = new CellTable<Permission>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrPermissions;
	@UiField NoneFoundPanel pnlNoPermissions;
	@UiField LoadingPanel pnlLoading;

	public PermissionsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblPermissions.setEmptyTableWidget(pnlNoPermissions);
		tblPermissions.setLoadingIndicator(pnlLoading);
		PermissionController.get().addDataDisplay(tblPermissions);
		pgrPermissions.setDisplay(tblPermissions);
	}

	private void createColumns () {
		TextColumn<Permission> code = new TextColumn<Permission>() {

			@Override
			public String getValue (Permission object) {
				return object.code;
			}
		};

		TextColumn<Permission> type = new TextColumn<Permission>() {

			@Override
			public String getValue (Permission object) {
				return object.type.toString();
			}
		};

		TextColumn<Permission> name = new TextColumn<Permission>() {

			@Override
			public String getValue (Permission object) {
				return object.name;
			}
		};

		TextColumn<Permission> description = new TextColumn<Permission>() {

			@Override
			public String getValue (Permission object) {
				return object.description;
			}
		};

		tblPermissions.addColumn(code, "Code");
		tblPermissions.addColumn(type, "Type");
		tblPermissions.addColumn(name, "Name");
		tblPermissions.addColumn(description, "Description");
	}

}

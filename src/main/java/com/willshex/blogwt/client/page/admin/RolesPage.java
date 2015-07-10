//
//  RolesPage.java
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
import com.willshex.blogwt.client.controller.RoleController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.helper.PagerHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RolesPage extends Page {

	private static RolesPageUiBinder uiBinder = GWT
			.create(RolesPageUiBinder.class);

	interface RolesPageUiBinder extends UiBinder<Widget, RolesPage> {}

	@UiField(provided = true) CellTable<Role> tblRoles = new CellTable<Role>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrRoles;
	@UiField NoneFoundPanel pnlNoRoles;

	public RolesPage () {
		super(PageType.RolesPageType);
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblRoles.setEmptyTableWidget(pnlNoRoles);
		RoleController.get().addDataDisplay(tblRoles);
		pgrRoles.setDisplay(tblRoles);
	}

	private void createColumns () {
		TextColumn<Role> code = new TextColumn<Role>() {

			@Override
			public String getValue (Role object) {
				return object.code;
			}
		};

		TextColumn<Role> name = new TextColumn<Role>() {

			@Override
			public String getValue (Role object) {
				return object.name;
			}
		};

		TextColumn<Role> description = new TextColumn<Role>() {

			@Override
			public String getValue (Role object) {
				return object.description;
			}
		};

		tblRoles.addColumn(code, "Code");
		tblRoles.addColumn(name, "Name");
		tblRoles.addColumn(description, "Description");
	}

}

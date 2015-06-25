//
//  UsersPage.java
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
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.PagerHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UsersPage extends Page {

	private static UsersPageUiBinder uiBinder = GWT
			.create(UsersPageUiBinder.class);

	interface UsersPageUiBinder extends UiBinder<Widget, UsersPage> {}

	@UiField(provided = true) CellTable<User> tblUsers = new CellTable<User>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrUsers;
	@UiField NoneFoundPanel pnlNoUsers;

	public UsersPage () {
		super(PageType.UsersPageType);
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblUsers.setEmptyTableWidget(pnlNoUsers);
		UserController.get().addDataDisplay(tblUsers);
		pgrUsers.setDisplay(tblUsers);
	}

	/**
	 * 
	 */
	private void createColumns () {
		TextColumn<User> username = new TextColumn<User>() {
			
			@Override
			public String getValue (User object) {
				return object.username;
			}
		};
		
		tblUsers.addColumn(username);
	}

}

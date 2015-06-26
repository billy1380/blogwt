//
//  UsersPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.PrettyButtonCell;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.helper.DateTimeHelper;
import com.willshex.blogwt.shared.api.helper.PagerHelper;
import com.willshex.blogwt.shared.api.helper.UserHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UsersPage extends Page {

	private static UsersPageUiBinder uiBinder = GWT
			.create(UsersPageUiBinder.class);

	interface UsersPageUiBinder extends UiBinder<Widget, UsersPage> {}

	public interface UsersPageTemplates extends SafeHtmlTemplates {
		public UsersPageTemplates INSTANCE = GWT
				.create(UsersPageTemplates.class);

		@Template("<a href=\"mailto:{0}\">{1}</a>")
		SafeHtml emailLink (String email, String emailDescription);
	}

	@UiField(provided = true) CellTable<User> tblUsers = new CellTable<User>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager pgrUsers;
	@UiField NoneFoundPanel pnlNoUsers;
	private ImageCell imagePrototype = new ImageCell();
	private Cell<SafeHtml> safeHtmlPrototype = new SafeHtmlCell();
	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

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

		Column<User, String> avatar = new Column<User, String>(imagePrototype) {

			@Override
			public String getValue (User object) {
				return object.avatar + "?s=" + UserHelper.AVATAR_HEADER_SIZE
						+ "&default=retro";
			}
		};
		tblUsers.setColumnWidth(avatar, 20.0, Unit.PX);

		TextColumn<User> username = new TextColumn<User>() {

			@Override
			public String getValue (User object) {
				return object.username;
			}
		};

		TextColumn<User> name = new TextColumn<User>() {

			@Override
			public String getValue (User object) {
				return UserHelper.name(object);
			}
		};

		Column<User, SafeHtml> email = new Column<User, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (User object) {
				return UsersPageTemplates.INSTANCE.emailLink(object.email,
						UserHelper.emailDescription(object));
			}
		};

		TextColumn<User> lastLoggedIn = new TextColumn<User>() {

			@Override
			public String getValue (User object) {
				return DateTimeHelper.ago(object.lastLoggedIn);
			}
		};

		Column<User, SafeHtml> edit = new Column<User, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (User object) {
				return SafeHtmlUtils
						.fromSafeConstant("<a class=\"btn btn-default btn-xs\" href=\""
								+ PageType.ChangeDetailsPageType.asHref("id",
										object.id.toString()).asString()
								+ "\" ><span class=\"glyphicon glyphicon-edit\"></span> edit<a>");
			}
		};
		tblUsers.setColumnWidth(edit, 1.0, Unit.PX);

		Column<User, String> delete = new Column<User, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (User object) {
				return "delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<User, String>() {

			@Override
			public void update (int index, User object, String value) {

			}
		});
		tblUsers.setColumnWidth(delete, 1.0, Unit.PX);

		Column<User, String> admin = new Column<User, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (User object) {
				return "admin";
			}
		};
		admin.setFieldUpdater(new FieldUpdater<User, String>() {

			@Override
			public void update (int index, User object, String value) {
				
			}
		});
		tblUsers.setColumnWidth(admin, 1.0, Unit.PX);

		tblUsers.addColumn(avatar);
		tblUsers.addColumn(username, "Username");
		tblUsers.addColumn(name, "Name");
		tblUsers.addColumn(email, "E-mail");
		tblUsers.addColumn(lastLoggedIn, "Last seen");
		tblUsers.addColumn(edit);
		tblUsers.addColumn(admin);
		tblUsers.addColumn(delete);
	}
}

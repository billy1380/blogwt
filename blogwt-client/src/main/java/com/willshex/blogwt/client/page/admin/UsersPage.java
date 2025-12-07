//
//  UsersPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.user.event.ChangeUserAccessEventHandler;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.SimplePager;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UsersPage extends Page implements ChangeUserAccessEventHandler {

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
	@UiField Button btnRefresh;
	@UiField LoadingPanel pnlLoading;

	public UsersPage () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblUsers.setEmptyTableWidget(pnlNoUsers);
		tblUsers.setLoadingIndicator(pnlLoading);
		UserController.get().addDataDisplay(tblUsers);
		pgrUsers.setDisplay(tblUsers);
	}

	/**
	 * create columns
	 */
	private void createColumns () {
		Column<User, String> avatar = new Column<User, String>(
				UiHelper.IMAGE_PROTOTYPE) {

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
				UiHelper.SAFE_HTML_PROTOTYPE) {
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
				UiHelper.SAFE_HTML_PROTOTYPE) {
			@Override
			public SafeHtml getValue (User object) {
				return UiHelper.TEMPLATES.xsEdit(
						PageTypeHelper.asHref(PageType.ChangeDetailsPageType,
								"id", object.id.toString()));
			}
		};
		tblUsers.setColumnWidth(edit, 1.0, Unit.PX);

		Column<User, String> suspend = new Column<User, String>(
				UiHelper.ACTION_PROTOTYPE) {
			@Override
			public String getValue (User object) {
				return UserHelper.isSuspended(object) ? "unsuspend" : "suspend";
			}
		};
		suspend.setFieldUpdater(new FieldUpdater<User, String>() {
			@Override
			public void update (int index, User object, String value) {
				boolean suspended = UserHelper.isSuspended(object);

				if (suspended) {
					UserController.get().unsuspendUser(object);
				} else {
					if (Window.confirm("Are you sure you want to suspend "
							+ object.username + "'s account indefinitely?")) {
						UserController.get().suspendUser(object);
					}
				}
			}
		});
		tblUsers.setColumnWidth(suspend, 1.0, Unit.PX);

		Column<User, String> admin = new Column<User, String>(
				UiHelper.ACTION_PROTOTYPE) {
			@Override
			public String getValue (User object) {
				return "admin";
			}
		};

		final Role adminRole = RoleHelper.createFullAdmin();

		admin.setFieldUpdater(new FieldUpdater<User, String>() {

			@Override
			public void update (int index, User object, String value) {
				if (Window.confirm("Are you sure you want to make "
						+ object.username + " a " + adminRole.name + "?")) {
					UserController.get().assignUserRoles(object, adminRole);
				}
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
		tblUsers.addColumn(suspend);
	}
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				ChangeUserAccessEventHandler.TYPE, UserController.get(), this));
	}

	private void refresh () {
		tblUsers.setVisibleRangeAndClearData(tblUsers.getVisibleRange(), true);
	}

	@UiHandler("btnRefresh")
	void onRefreshClicked (ClickEvent ce) {
		refresh();
	}
	@Override
	public void changeUserAccessSuccess (ChangeUserAccessRequest input,
			ChangeUserAccessResponse output) {
		refresh();
	}
	@Override
	public void changeUserAccessFailure (ChangeUserAccessRequest input,
			Throwable caught) {}
}

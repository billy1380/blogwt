//
//  ChangeAccessPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
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
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.user.event.ChangeUserAccessEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PermissionController;
import com.willshex.blogwt.client.controller.RoleController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.BootstrapGwtSuggestBox;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.accounttabs.AccountTabsPart;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author billy1380
 *
 */
public class ChangeAccessPage extends Page
		implements ChangeUserAccessEventHandler {

	public interface Templates extends SafeHtmlTemplates {
		Templates INSTANCE = GWT.create(Templates.class);

		@Template("<span class=\"label label-info\" title=\"{1}\"><strong>{0}</strong></span>")
		SafeHtml code (String code, String description);

	}

	private static ChangeAccessPageUiBinder uiBinder = GWT
			.create(ChangeAccessPageUiBinder.class);

	interface ChangeAccessPageUiBinder
			extends UiBinder<Widget, ChangeAccessPage> {}

	@UiField(provided = true) CellTable<Role> tblRoles = new CellTable<Role>(
			Integer.MAX_VALUE, BootstrapGwtCellTable.INSTANCE);
	@UiField(provided = true) CellTable<Permission> tblPermissions = new CellTable<Permission>(
			Integer.MAX_VALUE, BootstrapGwtCellTable.INSTANCE);
	@UiField(provided = true) SuggestBox txtRole = new SuggestBox(
			RoleController.get().oracle());
	@UiField(provided = true) SuggestBox txtPermission = new SuggestBox(
			PermissionController.get().oracle());
	@UiField NoneFoundPanel pnlNoRoles;
	@UiField NoneFoundPanel pnlNoPermissions;
	@UiField FormPanel frmChangeAccess;

	@UiField LoadingPanel pnlRolesLoading;
	@UiField LoadingPanel pnlPermissionsLoading;

	@UiField HTMLPanel pnlTabs;
	@UiField Element elAddRole;
	@UiField Element elAddPermission;

	private User user;
	private static final Permission MANAGE_USERS = PermissionHelper
			.create(PermissionHelper.MANAGE_USERS);
	private Column<Role, String> deleteRow;
	private Column<Permission, String> deletePermission;

	public ChangeAccessPage () {
		initWidget(uiBinder.createAndBindUi(this));

		BootstrapGwtSuggestBox.INSTANCE.styles().ensureInjected();

		UserController.roles().addDataDisplay(tblRoles);
		UserController.permissions().addDataDisplay(tblPermissions);

		tblRoles.setEmptyTableWidget(pnlNoRoles);
		tblRoles.setLoadingIndicator(pnlRolesLoading);
		createRoleColumns();

		tblPermissions.setEmptyTableWidget(pnlNoPermissions);
		tblPermissions.setLoadingIndicator(pnlPermissionsLoading);
		createPermissionColumns();

		canManage(false);
		
		UiHelper.addPlaceholder(txtRole, "e.g. administrator or ADM");
		UiHelper.addPlaceholder(txtPermission, "e.g. manage posts or MPO");
	}

	private void canManage (boolean isUserManager) {
		if (isUserManager) {
			elAddRole.getStyle().clearDisplay();
			elAddPermission.getStyle().clearDisplay();
			if (-1 == tblRoles.getColumnIndex(deleteRow)) {
				tblRoles.addColumn(deleteRow);
			}

			if (-1 == tblPermissions.getColumnIndex(deletePermission)) {
				tblPermissions.addColumn(deletePermission);
			}
		} else {
			elAddRole.getStyle().setDisplay(Display.NONE);
			elAddPermission.getStyle().setDisplay(Display.NONE);

			if (-1 != tblRoles.getColumnIndex(deleteRow)) {
				tblRoles.removeColumn(deleteRow);
			}

			if (-1 != tblPermissions.getColumnIndex(deletePermission)) {
				tblPermissions.removeColumn(deletePermission);
			}
		}
	}

	private void createRoleColumns () {
		Column<Role, SafeHtml> code = new Column<Role, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {

			@Override
			public SafeHtml getValue (Role object) {
				return Templates.INSTANCE.code(object.code, object.description);
			}
		};

		TextColumn<Role> name = new TextColumn<Role>() {

			@Override
			public String getValue (Role object) {
				return object.name;
			}
		};

		deleteRow = new Column<Role, String>(UiHelper.ACTION_PROTOTYPE) {

			@Override
			public String getValue (Role object) {
				return "delete";
			}
		};
		deleteRow.setFieldUpdater(new FieldUpdater<Role, String>() {

			@Override
			public void update (int index, Role object, String value) {
				if (Window.confirm("Are you sure you want to remove user role: "
						+ object.name + "?")) {
					UserController.get().revokeUserRoles(user, object);
				}
			}
		});
		deleteRow.setHorizontalAlignment(Column.ALIGN_RIGHT);

		tblRoles.addColumn(code);
		tblRoles.addColumn(name);
	}

	private void createPermissionColumns () {
		Column<Permission, SafeHtml> code = new Column<Permission, SafeHtml>(
				UiHelper.SAFE_HTML_PROTOTYPE) {

			@Override
			public SafeHtml getValue (Permission object) {
				return Templates.INSTANCE.code(object.code, object.description);
			}
		};

		TextColumn<Permission> name = new TextColumn<Permission>() {

			@Override
			public String getValue (Permission object) {
				return object.name;
			}
		};

		deletePermission = new Column<Permission, String>(
				UiHelper.ACTION_PROTOTYPE) {

			@Override
			public String getValue (Permission object) {
				return "delete";
			}
		};
		deletePermission
				.setFieldUpdater(new FieldUpdater<Permission, String>() {

					@Override
					public void update (int index, Permission object,
							String value) {
						if (Window.confirm(
								"Are you sure you want to revoke user permission: "
										+ object.name + "?")) {
							UserController.get().revokeUserPermissions(user,
									object);
						}
					}
				});
		deletePermission.setHorizontalAlignment(Column.ALIGN_RIGHT);

		tblPermissions.addColumn(code);
		tblPermissions.addColumn(name);
		tblPermissions.addColumn(deletePermission);
	}

	@UiHandler("btnAddRole")
	void onAddRoleClicked (ClickEvent ce) {
		loading();

		if (isValidRole()) {
			UserController.get().assignUserRoles(user,
					new Role().code(txtRole.getValue()));
		}
	}

	@UiHandler("btnAddPermission")
	void onAddPremissionClicked (ClickEvent ce) {
		loading();

		if (isValidPermission()) {
			UserController.get().assignUserPermissions(user,
					new Permission().code(txtPermission.getValue()));
		}
	}

	@Override
	protected void onAttach () {
		super.onAttach();

		pnlTabs.add(AccountTabsPart.get());

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					ready();

					boolean noUrlUser = false;
					User loggedIn = SessionController.get().user();
					User urlUser = null;
					if (c.getAction() == null) {
						user = loggedIn;
						noUrlUser = true;
					} else if (loggedIn != null && DataTypeHelper.same(loggedIn,
							urlUser = urlUser(c))) {
						user = loggedIn;
					} else {
						user = null;
					}

					if (user == null && urlUser != null) {
						user = urlUser;
					}

					AccountTabsPart.get().setUser(noUrlUser ? null : user);
					UserController.get().setUser(user);
					tblRoles.setVisibleRangeAndClearData(
							tblRoles.getVisibleRange(), true);
					tblPermissions.setVisibleRangeAndClearData(
							tblPermissions.getVisibleRange(), true);
				}));
		register(DefaultEventBus.get().addHandlerToSource(
				ChangeUserAccessEventHandler.TYPE, UserController.get(), this));

		canManage(SessionController.get().isAdmin()
				|| SessionController.get().isAuthorised(MANAGE_USERS));
	}

	private User urlUser (Stack c) {
		User urlUser = null;
		if ("id".equals(c.getAction()) && c.getParameterCount() > 0) {
			urlUser = (User) new User().id(Long.valueOf(c.getParameter(0)));
		}

		return urlUser;
	}

	private void loading () {

	}

	private void ready () {

	}
	@Override
	protected void reset () {
		frmChangeAccess.reset();
		canManage(false);
		super.reset();
	}
	@Override
	public void changeUserAccessSuccess (ChangeUserAccessRequest input,
			ChangeUserAccessResponse output) {
		if (DataTypeHelper.<User> same(input.user, user)) {
			if (output.status == StatusType.StatusTypeSuccess) {
				if (input.roles == null) {
					tblPermissions.setVisibleRangeAndClearData(
							tblPermissions.getVisibleRange(), true);
				} else {
					tblRoles.setVisibleRangeAndClearData(
							tblPermissions.getVisibleRange(), true);
				}

				reset();
			}

			ready();
		}
	}
	@Override
	public void changeUserAccessFailure (ChangeUserAccessRequest input,
			Throwable caught) {
		if (DataTypeHelper.same(input.user, user)) {
			GWT.log("Error changing user access", caught);

			ready();
		}
	}

	private boolean isValidRole () {
		return txtRole.getValue().length() == 3;
	}

	private boolean isValidPermission () {
		return txtPermission.getValue().length() == 3;
	}
}

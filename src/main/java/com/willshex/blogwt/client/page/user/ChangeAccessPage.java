//
//  ChangeAccessPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.user.event.ChangeUserAccessEventHandler;
import com.willshex.blogwt.client.cell.PrettyButtonCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.PermissionController;
import com.willshex.blogwt.client.controller.RoleController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.BootstrapGwtSuggestBox;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author billy1380
 *
 */
public class ChangeAccessPage extends Page
		implements NavigationChangedEventHandler, ChangeUserAccessEventHandler {

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

	private User user;
	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();
	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

	public ChangeAccessPage () {
		initWidget(uiBinder.createAndBindUi(this));

		BootstrapGwtSuggestBox.INSTANCE.styles().ensureInjected();

		UserController.roles().addDataDisplay(tblRoles);
		UserController.permissions().addDataDisplay(tblPermissions);

		tblRoles.setEmptyTableWidget(pnlNoRoles);
		createRoleColumns();

		tblPermissions.setEmptyTableWidget(pnlNoPermissions);
		createPermissionColumns();
	}

	private void createRoleColumns () {
		Column<Role, SafeHtml> code = new Column<Role, SafeHtml>(
				safeHtmlPrototype) {

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

		Column<Role, String> delete = new Column<Role, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (Role object) {
				return "delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<Role, String>() {

			@Override
			public void update (int index, Role object, String value) {
				if (Window.confirm("Are you sure you want to remove user role: "
						+ object.name + "?")) {
					UserController.get().revokeUserRoles(user, object);
				}
			}
		});
		delete.setHorizontalAlignment(Column.ALIGN_RIGHT);

		tblRoles.addColumn(code);
		tblRoles.addColumn(name);
		tblRoles.addColumn(delete);
	}

	private void createPermissionColumns () {
		Column<Permission, SafeHtml> code = new Column<Permission, SafeHtml>(
				safeHtmlPrototype) {

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

		Column<Permission, String> delete = new Column<Permission, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (Permission object) {
				return "delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<Permission, String>() {

			@Override
			public void update (int index, Permission object, String value) {
				if (Window.confirm(
						"Are you sure you want to revoke user permission: "
								+ object.name + "?")) {
					UserController.get().revokeUserPermissions(user, object);
				}
			}
		});
		delete.setHorizontalAlignment(Column.ALIGN_RIGHT);

		tblPermissions.addColumn(code);
		tblPermissions.addColumn(name);
		tblPermissions.addColumn(delete);
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

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
		register(DefaultEventBus.get().addHandlerToSource(
				ChangeUserAccessEventHandler.TYPE, UserController.get(), this));
	}

	@Override
	public void navigationChanged (Stack previous, Stack current) {
		ready();

		User loggedIn = SessionController.get().user();
		if (current.getAction() == null || (loggedIn != null
				&& loggedIn.id.equals(current.getAction()))) {
			user = loggedIn;
		} else {
			user = null;
		}

		if (user == null) {
			(user = new User()).id(Long.valueOf(current.getAction()));
		}

		UserController.get().setUser(user);
		tblRoles.setVisibleRangeAndClearData(tblRoles.getVisibleRange(), true);
		tblPermissions.setVisibleRangeAndClearData(
				tblPermissions.getVisibleRange(), true);
	}

	private void loading () {

	}

	private void ready () {

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		frmChangeAccess.reset();
		super.reset();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.
	 * ChangeUserAccessEventHandler
	 * #changeUserAccessSuccess(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserAccessRequest,
	 * com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse) */
	@Override
	public void changeUserAccessSuccess (ChangeUserAccessRequest input,
			ChangeUserAccessResponse output) {
		if (input.user.id != null && user != null
				&& input.user.id.equals(user.id)) {
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

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.
	 * ChangeUserAccessEventHandler
	 * #changeUserAccessFailure(com.willshex.blogwt.shared.api.user.call.
	 * ChangeUserAccessRequest, java.lang.Throwable) */
	@Override
	public void changeUserAccessFailure (ChangeUserAccessRequest input,
			Throwable caught) {
		if (input.user.id != null && user != null
				&& input.user.id.equals(user.id)) {
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

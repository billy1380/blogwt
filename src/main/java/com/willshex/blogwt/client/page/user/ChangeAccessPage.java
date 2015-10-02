//
//  ChangeAccessPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.user;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.controller.UserController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.event.GetRolesAndPermissionsEventHandler;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author billy1380
 *
 */
public class ChangeAccessPage extends Page implements
		NavigationChangedEventHandler, GetRolesAndPermissionsEventHandler {

	private static ChangeAccessPageUiBinder uiBinder = GWT
			.create(ChangeAccessPageUiBinder.class);

	interface ChangeAccessPageUiBinder extends
			UiBinder<Widget, ChangeAccessPage> {}

	@UiField(provided = true) CellTable<Role> ctRoles = new CellTable<Role>();
	@UiField(provided = true) CellTable<Permission> ctPermissions = new CellTable<Permission>();

	private ListDataProvider<Role> rolesProvider = new ListDataProvider<Role>();
	private ListDataProvider<Permission> permissionsProvider = new ListDataProvider<Permission>();
	private User user;

	public ChangeAccessPage () {
		initWidget(uiBinder.createAndBindUi(this));

		rolesProvider.addDataDisplay(ctRoles);
		permissionsProvider.addDataDisplay(ctPermissions);
	}

	@UiHandler("btnAddRole")
	void onAddRoleClicked (ClickEvent ce) {
		submitting();
	}

	@UiHandler("btnAddPermission")
	void onAddPremissionClicked (ClickEvent ce) {
		submitting();
	}

	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this));
	}

	@Override
	public void navigationChanged (Stack previous, Stack current) {
		User loggedIn = SessionController.get().user();
		if (current.getAction() == null
				|| (loggedIn != null && loggedIn.id.equals(current.getAction()))) {
			user = loggedIn;
		} else {
			user = null;
		}

		if (user == null) {
			(user = new User()).id(Long.valueOf(current.getAction()));
			UserController.get().getUserRolesAndPremissions(user);

			loading();
		} else {
			show(user.roles, user.permissions);
		}
	}

	private void show (List<Role> roles, List<Permission> permissions) {
		if (roles == null) {
			rolesProvider.setList(Collections.<Role> emptyList());
		} else {
			rolesProvider.setList(roles);
		}

		if (permissions == null) {
			permissionsProvider.setList(Collections.<Permission> emptyList());
		} else {
			permissionsProvider.setList(permissions);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.
	 * GetRolesAndPermissionsEventHandler
	 * #getRolesAndPermissionsSuccess(com.willshex
	 * .blogwt.shared.api.user.call.GetRolesAndPermissionsRequest,
	 * com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse) */
	@Override
	public void getRolesAndPermissionsSuccess (
			GetRolesAndPermissionsRequest input,
			GetRolesAndPermissionsResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			show(output.roles, output.permissions);
		} else {
			GWT.log(output.error.toString());
		}

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.shared.api.user.call.event.
	 * GetRolesAndPermissionsEventHandler
	 * #getRolesAndPermissionsFailure(com.willshex
	 * .blogwt.shared.api.user.call.GetRolesAndPermissionsRequest,
	 * java.lang.Throwable) */
	@Override
	public void getRolesAndPermissionsFailure (
			GetRolesAndPermissionsRequest input, Throwable caught) {
		GWT.log("GetRolesAndPermissions", caught);

		ready();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#reset() */
	@Override
	protected void reset () {
		super.reset();
	}

	private void loading () {

	}

	private void submitting () {

	}

	private void ready () {

	}
}

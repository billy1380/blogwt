//
//  UserController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserPowersRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserPowersResponse;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordResponse;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse;
import com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler.ChangePasswordFailure;
import com.willshex.blogwt.shared.api.user.call.event.ChangePasswordEventHandler.ChangePasswordSuccess;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler.ChangeUserDetailsFailure;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserDetailsEventHandler.ChangeUserDetailsSuccess;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserPowersEventHandler.ChangeUserPowersFailure;
import com.willshex.blogwt.shared.api.user.call.event.ChangeUserPowersEventHandler.ChangeUserPowersSuccess;
import com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler.GetEmailAvatarFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetEmailAvatarEventHandler.GetEmailAvatarSuccess;
import com.willshex.blogwt.shared.api.user.call.event.GetRolesAndPermissionsEventHandler.GetRolesAndPermissionsFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetRolesAndPermissionsEventHandler.GetRolesAndPermissionsSuccess;
import com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler.GetUserDetailsFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetUserDetailsEventHandler.GetUserDetailsSuccess;
import com.willshex.blogwt.shared.api.user.call.event.GetUsersEventHandler.GetUsersFailure;
import com.willshex.blogwt.shared.api.user.call.event.GetUsersEventHandler.GetUsersSuccess;
import com.willshex.blogwt.shared.api.user.call.event.RegisterUserEventHandler.RegisterUserFailure;
import com.willshex.blogwt.shared.api.user.call.event.RegisterUserEventHandler.RegisterUserSuccess;
import com.willshex.blogwt.shared.api.user.call.event.ResetPasswordEventHandler.ResetPasswordFailure;
import com.willshex.blogwt.shared.api.user.call.event.ResetPasswordEventHandler.ResetPasswordSuccess;
import com.willshex.blogwt.shared.api.user.call.event.VerifyAccountEventHandler.VerifyAccountFailure;
import com.willshex.blogwt.shared.api.user.call.event.VerifyAccountEventHandler.VerifyAccountSuccess;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.json.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class UserController extends AsyncDataProvider<User> {

	private AsyncDataProvider<Role> USER_ROLES_PROVIDER = new AsyncDataProvider<Role>() {

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com
		 * .google.gwt.view.client.HasData) */
		@Override
		protected void onRangeChanged (HasData<Role> display) {

		}

	};

	private AsyncDataProvider<Permission> USER_PERMISSIONS_PROVIDER = new AsyncDataProvider<Permission>() {

		/* (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com
		 * .google.gwt.view.client.HasData) */
		@Override
		protected void onRangeChanged (HasData<Permission> display) {
			UserController.this.fetchUserRolesAndPremissions();
		}

	};

	private static UserController one = null;

	public static UserController get () {
		if (one == null) {
			one = new UserController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager().sortBy(
			UserSortType.UserSortTypeAdded.toString());

	private User user;

	private Request getUsersRequest;
	private Request getUserRequest;
	private Request getEmailAvatarRequest;
	private Request getRolesAndPermissionsRequest;

	private void fetchUsers () {
		final GetUsersRequest input = ApiHelper
				.setAccessCode(new GetUsersRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getUsersRequest != null) {
			getUsersRequest.cancel();
		}

		getUsersRequest = ApiHelper.createUserClient().getUsers(input,
				new AsyncCallback<GetUsersResponse>() {

					@Override
					public void onSuccess (GetUsersResponse output) {
						getUsersRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.users != null && output.users.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.users);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<User> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetUsersSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getUsersRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetUsersFailure(input, caught),
								UserController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<User> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart())).count(
				Integer.valueOf(range.getLength()));

		fetchUsers();
	}

	/**
	 * @param user
	 */
	public void getUser (User user) {
		final GetUserDetailsRequest input = ApiHelper
				.setAccessCode(new GetUserDetailsRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.user = user;

		if (getUserRequest != null) {
			getUserRequest.cancel();
		}

		getUserRequest = ApiHelper.createUserClient().getUserDetails(input,
				new AsyncCallback<GetUserDetailsResponse>() {

					@Override
					public void onSuccess (GetUserDetailsResponse output) {
						getUserRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new GetUserDetailsSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetUserDetailsFailure(input, caught),
								UserController.this);
					}

				});
	}

	/**
	 * @param user
	 */
	public void changeUserDetails (User user) {
		final ChangeUserDetailsRequest input = ApiHelper
				.setAccessCode(new ChangeUserDetailsRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.user = user;

		ApiHelper.createUserClient().changeUserDetails(input,
				new AsyncCallback<ChangeUserDetailsResponse>() {

					@Override
					public void onSuccess (ChangeUserDetailsResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new ChangeUserDetailsSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new ChangeUserDetailsFailure(input, caught),
								UserController.this);
					}
				});
	}

	public void changeUserPassword (Long userId, String password,
			String newPassword) {
		final ChangePasswordRequest input = ApiHelper
				.setAccessCode(new ChangePasswordRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.password = password;
		input.changedPassword = newPassword;

		// change password should take a user parameter

		ApiHelper.createUserClient().changePassword(input,
				new AsyncCallback<ChangePasswordResponse>() {

					@Override
					public void onSuccess (ChangePasswordResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new ChangePasswordSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new ChangePasswordFailure(input, caught),
								UserController.this);
					}
				});
	}

	/**
	 * @param actionCode
	 * @param newPassword
	 */
	public void changeUserPassword (String actionCode, String newPassword) {
		final ChangePasswordRequest input = ApiHelper
				.setAccessCode(new ChangePasswordRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.resetCode = actionCode;
		input.changedPassword = newPassword;

		// change password should take a user parameter

		ApiHelper.createUserClient().changePassword(input,
				new AsyncCallback<ChangePasswordResponse>() {

					@Override
					public void onSuccess (ChangePasswordResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new ChangePasswordSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new ChangePasswordFailure(input, caught),
								UserController.this);
					}
				});
	}

	/**
	 * @param email
	 */
	public void resetPassword (String email) {
		final ResetPasswordRequest input = ApiHelper
				.setAccessCode(new ResetPasswordRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.email = email;

		ApiHelper.createUserClient().resetPassword(input,
				new AsyncCallback<ResetPasswordResponse>() {

					@Override
					public void onSuccess (ResetPasswordResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new ResetPasswordSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new ResetPasswordFailure(input, caught),
								UserController.this);
					}

				});
	}

	/**
	 * @param email
	 */
	public void getEmailAvatar (String email) {
		final GetEmailAvatarRequest input = ApiHelper
				.setAccessCode(new GetEmailAvatarRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.email = email;

		if (getEmailAvatarRequest != null) {
			getEmailAvatarRequest.cancel();
		}

		getEmailAvatarRequest = ApiHelper.createUserClient().getEmailAvatar(
				input, new AsyncCallback<GetEmailAvatarResponse>() {

					@Override
					public void onSuccess (GetEmailAvatarResponse output) {
						getEmailAvatarRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new GetEmailAvatarSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetEmailAvatarFailure(input, caught),
								UserController.this);
					}

				});
	}

	/**
	 * @param user
	 */
	public void registerUser (User user) {
		final RegisterUserRequest input = ApiHelper
				.setAccessCode(new RegisterUserRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.user = user;

		ApiHelper.createUserClient().registerUser(input,
				new AsyncCallback<RegisterUserResponse>() {

					@Override
					public void onSuccess (RegisterUserResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new RegisterUserSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new RegisterUserFailure(input, caught),
								UserController.this);
					}
				});
	}

	/**
	 * @param assign
	 * @param user
	 * @param roles
	 */
	public void changeUserRoles (boolean assign, User user, Role... roles) {
		changeUserPowers(assign, user, null, Arrays.asList(roles));
	}

	/**
	 * @param assign
	 * @param user
	 * @param permissions
	 */
	public void changeUserPermissions (boolean assign, User user,
			Permission... permissions) {
		changeUserPowers(assign, user, Arrays.asList(permissions), null);
	}

	private void changeUserPowers (boolean assign, User user,
			List<Permission> permissions, List<Role> roles) {
		final ChangeUserPowersRequest input = ApiHelper
				.setAccessCode(new ChangeUserPowersRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.user = user;
		input.assign = Boolean.valueOf(assign);
		input.roles = roles;
		input.permissions = permissions;

		ApiHelper.createUserClient().changeUserPowers(input,
				new AsyncCallback<ChangeUserPowersResponse>() {

					@Override
					public void onSuccess (ChangeUserPowersResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new ChangeUserPowersSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new ChangeUserPowersFailure(input, caught),
								UserController.this);
					}
				});
	}

	/**
	 * @param actionCode
	 */
	public void verifyAccount (String actionCode) {
		final VerifyAccountRequest input = ApiHelper
				.setAccessCode(new VerifyAccountRequest());

		input.session = SessionController.get().sessionForApiCall();
		input.actionCode = actionCode;

		ApiHelper.createUserClient().verifyAccount(input,
				new AsyncCallback<VerifyAccountResponse>() {

					@Override
					public void onSuccess (VerifyAccountResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

						}

						DefaultEventBus.get().fireEventFromSource(
								new VerifyAccountSuccess(input, output),
								UserController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new VerifyAccountFailure(input, caught),
								UserController.this);
					}
				});
	}

	/**
	 * @param user
	 */
	private void fetchUserRolesAndPremissions () {
		if (user != null) {
			if (getRolesAndPermissionsRequest != null) {
				getRolesAndPermissionsRequest.cancel();
				getRolesAndPermissionsRequest = null;
			}

			final GetRolesAndPermissionsRequest input = ApiHelper
					.setAccessCode(new GetRolesAndPermissionsRequest());

			input.session = SessionController.get().sessionForApiCall();
			input.user = user;

			getRolesAndPermissionsRequest = ApiHelper
					.createUserClient()
					.getRolesAndPermissions(
							input,
							new AsyncCallback<GetRolesAndPermissionsResponse>() {

								@Override
								public void onSuccess (
										GetRolesAndPermissionsResponse output) {
									getRolesAndPermissionsRequest = null;

									if (output.status == StatusType.StatusTypeSuccess) {
										if (output.roles != null) {
											USER_ROLES_PROVIDER.updateRowData(
													0, output.roles);
										} else {
											USER_ROLES_PROVIDER.updateRowCount(
													0, true);
										}

										if (output.permissions != null) {
											USER_PERMISSIONS_PROVIDER
													.updateRowData(0,
															output.permissions);
										} else {
											USER_PERMISSIONS_PROVIDER
													.updateRowCount(0, true);
										}
									}

									DefaultEventBus.get().fireEventFromSource(
											new GetRolesAndPermissionsSuccess(
													input, output),
											UserController.this);
								}

								@Override
								public void onFailure (Throwable caught) {
									getRolesAndPermissionsRequest = null;

									DefaultEventBus.get().fireEventFromSource(
											new GetRolesAndPermissionsFailure(
													input, caught),
											UserController.this);
								}
							});
		}
	}

	public static AsyncDataProvider<Role> roles () {
		return UserController.get().USER_ROLES_PROVIDER;
	}

	public static AsyncDataProvider<Permission> permissions () {
		return UserController.get().USER_PERMISSIONS_PROVIDER;
	}

	public void setUser (User value) {
		user = value;
	}
}

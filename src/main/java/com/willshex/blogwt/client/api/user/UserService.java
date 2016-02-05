//  
//  User/UserService.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.user.call.BlockUsersRequest;
import com.willshex.blogwt.shared.api.user.call.BlockUsersResponse;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameResponse;
import com.willshex.blogwt.shared.api.user.call.FollowUsersRequest;
import com.willshex.blogwt.shared.api.user.call.FollowUsersResponse;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordResponse;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedRequest;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedResponse;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutResponse;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordResponse;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class UserService extends JsonService {

	public static final String UserMethodBlockUsers = "BlockUsers";

	public Request blockUsers (final BlockUsersRequest input,
			final AsyncCallback<BlockUsersResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodBlockUsers, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								BlockUsersResponse outputParameter = new BlockUsersResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodBlockUsers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodBlockUsers, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodBlockUsers, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodBlockUsers, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodBlockUsers, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodFollowUsers = "FollowUsers";

	public Request followUsers (final FollowUsersRequest input,
			final AsyncCallback<FollowUsersResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodFollowUsers, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								FollowUsersResponse outputParameter = new FollowUsersResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodFollowUsers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodFollowUsers, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodFollowUsers, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodFollowUsers, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodFollowUsers, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodChangeUserAccess = "ChangeUserAccess";

	public Request changeUserAccess (final ChangeUserAccessRequest input,
			final AsyncCallback<ChangeUserAccessResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodChangeUserAccess, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								ChangeUserAccessResponse outputParameter = new ChangeUserAccessResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodChangeUserAccess, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodChangeUserAccess, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodChangeUserAccess, input,
									exception);
						}
					});
			onCallStart(UserService.this, UserMethodChangeUserAccess, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodChangeUserAccess, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetRoles = "GetRoles";

	public Request getRoles (final GetRolesRequest input,
			final AsyncCallback<GetRolesResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodGetRoles, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetRolesResponse outputParameter = new GetRolesResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodGetRoles, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodGetRoles, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this, UserMethodGetRoles,
									input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetRoles, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodGetRoles, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetPermissions = "GetPermissions";

	public Request getPermissions (final GetPermissionsRequest input,
			final AsyncCallback<GetPermissionsResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodGetPermissions, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetPermissionsResponse outputParameter = new GetPermissionsResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodGetPermissions, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodGetPermissions, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodGetPermissions, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetPermissions, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodGetPermissions, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetRolesAndPermissions = "GetRolesAndPermissions";

	public Request getRolesAndPermissions (
			final GetRolesAndPermissionsRequest input,
			final AsyncCallback<GetRolesAndPermissionsResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodGetRolesAndPermissions, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetRolesAndPermissionsResponse outputParameter = new GetRolesAndPermissionsResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodGetRolesAndPermissions, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodGetRolesAndPermissions, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodGetRolesAndPermissions, input,
									exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetRolesAndPermissions,
					input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodGetRolesAndPermissions,
					input, exception);
		}
		return handle;
	}

	public static final String UserMethodVerifyAccount = "VerifyAccount";

	public Request verifyAccount (final VerifyAccountRequest input,
			final AsyncCallback<VerifyAccountResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodVerifyAccount, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								VerifyAccountResponse outputParameter = new VerifyAccountResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodVerifyAccount, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodVerifyAccount, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodVerifyAccount, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodVerifyAccount, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodVerifyAccount, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodResetPassword = "ResetPassword";

	public Request resetPassword (final ResetPasswordRequest input,
			final AsyncCallback<ResetPasswordResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodResetPassword, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								ResetPasswordResponse outputParameter = new ResetPasswordResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodResetPassword, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodResetPassword, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodResetPassword, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodResetPassword, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodResetPassword, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetEmailAvatar = "GetEmailAvatar";

	public Request getEmailAvatar (final GetEmailAvatarRequest input,
			final AsyncCallback<GetEmailAvatarResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodGetEmailAvatar, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetEmailAvatarResponse outputParameter = new GetEmailAvatarResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodGetEmailAvatar, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodGetEmailAvatar, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodGetEmailAvatar, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetEmailAvatar, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodGetEmailAvatar, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodRegisterUser = "RegisterUser";

	public Request registerUser (final RegisterUserRequest input,
			final AsyncCallback<RegisterUserResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodRegisterUser, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								RegisterUserResponse outputParameter = new RegisterUserResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodRegisterUser, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodRegisterUser, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodRegisterUser, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodRegisterUser, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodRegisterUser, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodChangeUserDetails = "ChangeUserDetails";

	public Request changeUserDetails (final ChangeUserDetailsRequest input,
			final AsyncCallback<ChangeUserDetailsResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodChangeUserDetails, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								ChangeUserDetailsResponse outputParameter = new ChangeUserDetailsResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodChangeUserDetails, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodChangeUserDetails, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodChangeUserDetails, input,
									exception);
						}
					});
			onCallStart(UserService.this, UserMethodChangeUserDetails, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodChangeUserDetails, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetUsers = "GetUsers";

	public Request getUsers (final GetUsersRequest input,
			final AsyncCallback<GetUsersResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodGetUsers, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetUsersResponse outputParameter = new GetUsersResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodGetUsers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodGetUsers, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this, UserMethodGetUsers,
									input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetUsers, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodGetUsers, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodLogin = "Login";

	public Request login (final LoginRequest input,
			final AsyncCallback<LoginResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodLogin, input, new RequestCallback() {
				@Override
				public void onResponseReceived (Request request,
						Response response) {
					try {
						LoginResponse outputParameter = new LoginResponse();
						parseResponse(response, outputParameter);
						callback.onSuccess(outputParameter);
						onCallSuccess(UserService.this, UserMethodLogin, input,
								outputParameter);
					} catch (JSONException | HttpException exception) {
						callback.onFailure(exception);
						onCallFailure(UserService.this, UserMethodLogin, input,
								exception);
					}
				}

				@Override
				public void onError (Request request, Throwable exception) {
					callback.onFailure(exception);
					onCallFailure(UserService.this, UserMethodLogin, input,
							exception);
				}
			});
			onCallStart(UserService.this, UserMethodLogin, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodLogin, input, exception);
		}
		return handle;
	}

	public static final String UserMethodLogout = "Logout";

	public Request logout (final LogoutRequest input,
			final AsyncCallback<LogoutResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodLogout, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								LogoutResponse outputParameter = new LogoutResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodLogout, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodLogout, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this, UserMethodLogout,
									input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodLogout, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodLogout, input, exception);
		}
		return handle;
	}

	public static final String UserMethodChangePassword = "ChangePassword";

	public Request changePassword (final ChangePasswordRequest input,
			final AsyncCallback<ChangePasswordResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodChangePassword, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								ChangePasswordResponse outputParameter = new ChangePasswordResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodChangePassword, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodChangePassword, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodChangePassword, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodChangePassword, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodChangePassword, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodCheckUsername = "CheckUsername";

	public Request checkUsername (final CheckUsernameRequest input,
			final AsyncCallback<CheckUsernameResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodCheckUsername, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								CheckUsernameResponse outputParameter = new CheckUsernameResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodCheckUsername, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodCheckUsername, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodCheckUsername, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodCheckUsername, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodCheckUsername, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodIsAuthorised = "IsAuthorised";

	public Request isAuthorised (final IsAuthorisedRequest input,
			final AsyncCallback<IsAuthorisedResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodIsAuthorised, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								IsAuthorisedResponse outputParameter = new IsAuthorisedResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodIsAuthorised, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodIsAuthorised, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodIsAuthorised, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodIsAuthorised, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodIsAuthorised, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetUserDetails = "GetUserDetails";

	public Request getUserDetails (final GetUserDetailsRequest input,
			final AsyncCallback<GetUserDetailsResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodGetUserDetails, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetUserDetailsResponse outputParameter = new GetUserDetailsResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodGetUserDetails, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodGetUserDetails, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodGetUserDetails, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetUserDetails, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodGetUserDetails, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodForgotPassword = "ForgotPassword";

	public Request forgotPassword (final ForgotPasswordRequest input,
			final AsyncCallback<ForgotPasswordResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodForgotPassword, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								ForgotPasswordResponse outputParameter = new ForgotPasswordResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(UserService.this,
										UserMethodForgotPassword, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(UserService.this,
										UserMethodForgotPassword, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(UserService.this,
									UserMethodForgotPassword, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodForgotPassword, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(UserService.this, UserMethodForgotPassword, input,
					exception);
		}
		return handle;
	}
}
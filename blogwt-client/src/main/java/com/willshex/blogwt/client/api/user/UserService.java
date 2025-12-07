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
	public static final String UserMethodChangeUserAccess = "ChangeUserAccess";

	public Request changeUserAccess (ChangeUserAccessRequest input) {
		return changeUserAccess(input, null, null);
	}

	public Request changeUserAccess (ChangeUserAccessRequest input,
			AsyncSuccess<ChangeUserAccessRequest, ChangeUserAccessResponse> onSuccess) {
		return changeUserAccess(input, onSuccess, null);
	}

	public Request changeUserAccess (ChangeUserAccessRequest input,
			final AsyncCallback<ChangeUserAccessResponse> callback) {
		return changeUserAccess(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request changeUserAccess (ChangeUserAccessRequest input,
			AsyncSuccess<ChangeUserAccessRequest, ChangeUserAccessResponse> onSuccess,
			AsyncFailure<ChangeUserAccessRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodChangeUserAccess, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodChangeUserAccess, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodChangeUserAccess, input,
									exception);
						}
					});
			onCallStart(UserService.this, UserMethodChangeUserAccess, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodChangeUserAccess, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodLogin = "Login";

	public Request login (LoginRequest input) {
		return login(input, null, null);
	}

	public Request login (LoginRequest input,
			AsyncSuccess<LoginRequest, LoginResponse> onSuccess) {
		return login(input, onSuccess, null);
	}

	public Request login (LoginRequest input,
			final AsyncCallback<LoginResponse> callback) {
		return login(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request login (LoginRequest input,
			AsyncSuccess<LoginRequest, LoginResponse> onSuccess,
			AsyncFailure<LoginRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(UserMethodLogin, input, new RequestCallback() {
				@Override
				public void onResponseReceived (Request request,
						Response response) {
					try {
						LoginResponse outputParameter = new LoginResponse();
						parseResponse(response, outputParameter);
						if (onSuccess != null) {
							onSuccess.call(input, outputParameter);
						}

						onCallSuccess(UserService.this, UserMethodLogin, input,
								outputParameter);
					} catch (JSONException | HttpException exception) {
						if (onFailure != null) {
							onFailure.call(input, exception);
						}

						onCallFailure(UserService.this, UserMethodLogin, input,
								exception);
					}
				}

				@Override
				public void onError (Request request, Throwable exception) {
					if (onFailure != null) {
						onFailure.call(input, exception);
					}

					onCallFailure(UserService.this, UserMethodLogin, input,
							exception);
				}
			});
			onCallStart(UserService.this, UserMethodLogin, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodLogin, input, exception);
		}
		return handle;
	}

	public static final String UserMethodGetUsers = "GetUsers";

	public Request getUsers (GetUsersRequest input) {
		return getUsers(input, null, null);
	}

	public Request getUsers (GetUsersRequest input,
			AsyncSuccess<GetUsersRequest, GetUsersResponse> onSuccess) {
		return getUsers(input, onSuccess, null);
	}

	public Request getUsers (GetUsersRequest input,
			final AsyncCallback<GetUsersResponse> callback) {
		return getUsers(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getUsers (GetUsersRequest input,
			AsyncSuccess<GetUsersRequest, GetUsersResponse> onSuccess,
			AsyncFailure<GetUsersRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodGetUsers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodGetUsers, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this, UserMethodGetUsers,
									input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetUsers, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodGetUsers, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodBlockUsers = "BlockUsers";

	public Request blockUsers (BlockUsersRequest input) {
		return blockUsers(input, null, null);
	}

	public Request blockUsers (BlockUsersRequest input,
			AsyncSuccess<BlockUsersRequest, BlockUsersResponse> onSuccess) {
		return blockUsers(input, onSuccess, null);
	}

	public Request blockUsers (BlockUsersRequest input,
			final AsyncCallback<BlockUsersResponse> callback) {
		return blockUsers(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request blockUsers (BlockUsersRequest input,
			AsyncSuccess<BlockUsersRequest, BlockUsersResponse> onSuccess,
			AsyncFailure<BlockUsersRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodBlockUsers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodBlockUsers, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodBlockUsers, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodBlockUsers, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodBlockUsers, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodFollowUsers = "FollowUsers";

	public Request followUsers (FollowUsersRequest input) {
		return followUsers(input, null, null);
	}

	public Request followUsers (FollowUsersRequest input,
			AsyncSuccess<FollowUsersRequest, FollowUsersResponse> onSuccess) {
		return followUsers(input, onSuccess, null);
	}

	public Request followUsers (FollowUsersRequest input,
			final AsyncCallback<FollowUsersResponse> callback) {
		return followUsers(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request followUsers (FollowUsersRequest input,
			AsyncSuccess<FollowUsersRequest, FollowUsersResponse> onSuccess,
			AsyncFailure<FollowUsersRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodFollowUsers, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodFollowUsers, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodFollowUsers, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodFollowUsers, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodFollowUsers, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetRoles = "GetRoles";

	public Request getRoles (GetRolesRequest input) {
		return getRoles(input, null, null);
	}

	public Request getRoles (GetRolesRequest input,
			AsyncSuccess<GetRolesRequest, GetRolesResponse> onSuccess) {
		return getRoles(input, onSuccess, null);
	}

	public Request getRoles (GetRolesRequest input,
			final AsyncCallback<GetRolesResponse> callback) {
		return getRoles(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getRoles (GetRolesRequest input,
			AsyncSuccess<GetRolesRequest, GetRolesResponse> onSuccess,
			AsyncFailure<GetRolesRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodGetRoles, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodGetRoles, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this, UserMethodGetRoles,
									input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetRoles, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodGetRoles, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetPermissions = "GetPermissions";

	public Request getPermissions (GetPermissionsRequest input) {
		return getPermissions(input, null, null);
	}

	public Request getPermissions (GetPermissionsRequest input,
			AsyncSuccess<GetPermissionsRequest, GetPermissionsResponse> onSuccess) {
		return getPermissions(input, onSuccess, null);
	}

	public Request getPermissions (GetPermissionsRequest input,
			final AsyncCallback<GetPermissionsResponse> callback) {
		return getPermissions(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getPermissions (GetPermissionsRequest input,
			AsyncSuccess<GetPermissionsRequest, GetPermissionsResponse> onSuccess,
			AsyncFailure<GetPermissionsRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodGetPermissions, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodGetPermissions, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodGetPermissions, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetPermissions, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodGetPermissions, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetRolesAndPermissions = "GetRolesAndPermissions";

	public Request getRolesAndPermissions (
			GetRolesAndPermissionsRequest input) {
		return getRolesAndPermissions(input, null, null);
	}

	public Request getRolesAndPermissions (GetRolesAndPermissionsRequest input,
			AsyncSuccess<GetRolesAndPermissionsRequest, GetRolesAndPermissionsResponse> onSuccess) {
		return getRolesAndPermissions(input, onSuccess, null);
	}

	public Request getRolesAndPermissions (GetRolesAndPermissionsRequest input,
			final AsyncCallback<GetRolesAndPermissionsResponse> callback) {
		return getRolesAndPermissions(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getRolesAndPermissions (GetRolesAndPermissionsRequest input,
			AsyncSuccess<GetRolesAndPermissionsRequest, GetRolesAndPermissionsResponse> onSuccess,
			AsyncFailure<GetRolesAndPermissionsRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodGetRolesAndPermissions, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodGetRolesAndPermissions, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodGetRolesAndPermissions, input,
									exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetRolesAndPermissions,
					input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodGetRolesAndPermissions,
					input, exception);
		}
		return handle;
	}

	public static final String UserMethodVerifyAccount = "VerifyAccount";

	public Request verifyAccount (VerifyAccountRequest input) {
		return verifyAccount(input, null, null);
	}

	public Request verifyAccount (VerifyAccountRequest input,
			AsyncSuccess<VerifyAccountRequest, VerifyAccountResponse> onSuccess) {
		return verifyAccount(input, onSuccess, null);
	}

	public Request verifyAccount (VerifyAccountRequest input,
			final AsyncCallback<VerifyAccountResponse> callback) {
		return verifyAccount(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request verifyAccount (VerifyAccountRequest input,
			AsyncSuccess<VerifyAccountRequest, VerifyAccountResponse> onSuccess,
			AsyncFailure<VerifyAccountRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodVerifyAccount, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodVerifyAccount, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodVerifyAccount, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodVerifyAccount, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodVerifyAccount, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodResetPassword = "ResetPassword";

	public Request resetPassword (ResetPasswordRequest input) {
		return resetPassword(input, null, null);
	}

	public Request resetPassword (ResetPasswordRequest input,
			AsyncSuccess<ResetPasswordRequest, ResetPasswordResponse> onSuccess) {
		return resetPassword(input, onSuccess, null);
	}

	public Request resetPassword (ResetPasswordRequest input,
			final AsyncCallback<ResetPasswordResponse> callback) {
		return resetPassword(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request resetPassword (ResetPasswordRequest input,
			AsyncSuccess<ResetPasswordRequest, ResetPasswordResponse> onSuccess,
			AsyncFailure<ResetPasswordRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodResetPassword, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodResetPassword, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodResetPassword, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodResetPassword, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodResetPassword, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetEmailAvatar = "GetEmailAvatar";

	public Request getEmailAvatar (GetEmailAvatarRequest input) {
		return getEmailAvatar(input, null, null);
	}

	public Request getEmailAvatar (GetEmailAvatarRequest input,
			AsyncSuccess<GetEmailAvatarRequest, GetEmailAvatarResponse> onSuccess) {
		return getEmailAvatar(input, onSuccess, null);
	}

	public Request getEmailAvatar (GetEmailAvatarRequest input,
			final AsyncCallback<GetEmailAvatarResponse> callback) {
		return getEmailAvatar(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getEmailAvatar (GetEmailAvatarRequest input,
			AsyncSuccess<GetEmailAvatarRequest, GetEmailAvatarResponse> onSuccess,
			AsyncFailure<GetEmailAvatarRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodGetEmailAvatar, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodGetEmailAvatar, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodGetEmailAvatar, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetEmailAvatar, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodGetEmailAvatar, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodRegisterUser = "RegisterUser";

	public Request registerUser (RegisterUserRequest input) {
		return registerUser(input, null, null);
	}

	public Request registerUser (RegisterUserRequest input,
			AsyncSuccess<RegisterUserRequest, RegisterUserResponse> onSuccess) {
		return registerUser(input, onSuccess, null);
	}

	public Request registerUser (RegisterUserRequest input,
			final AsyncCallback<RegisterUserResponse> callback) {
		return registerUser(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request registerUser (RegisterUserRequest input,
			AsyncSuccess<RegisterUserRequest, RegisterUserResponse> onSuccess,
			AsyncFailure<RegisterUserRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodRegisterUser, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodRegisterUser, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodRegisterUser, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodRegisterUser, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodRegisterUser, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodChangeUserDetails = "ChangeUserDetails";

	public Request changeUserDetails (ChangeUserDetailsRequest input) {
		return changeUserDetails(input, null, null);
	}

	public Request changeUserDetails (ChangeUserDetailsRequest input,
			AsyncSuccess<ChangeUserDetailsRequest, ChangeUserDetailsResponse> onSuccess) {
		return changeUserDetails(input, onSuccess, null);
	}

	public Request changeUserDetails (ChangeUserDetailsRequest input,
			final AsyncCallback<ChangeUserDetailsResponse> callback) {
		return changeUserDetails(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request changeUserDetails (ChangeUserDetailsRequest input,
			AsyncSuccess<ChangeUserDetailsRequest, ChangeUserDetailsResponse> onSuccess,
			AsyncFailure<ChangeUserDetailsRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodChangeUserDetails, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodChangeUserDetails, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodChangeUserDetails, input,
									exception);
						}
					});
			onCallStart(UserService.this, UserMethodChangeUserDetails, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodChangeUserDetails, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodLogout = "Logout";

	public Request logout (LogoutRequest input) {
		return logout(input, null, null);
	}

	public Request logout (LogoutRequest input,
			AsyncSuccess<LogoutRequest, LogoutResponse> onSuccess) {
		return logout(input, onSuccess, null);
	}

	public Request logout (LogoutRequest input,
			final AsyncCallback<LogoutResponse> callback) {
		return logout(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request logout (LogoutRequest input,
			AsyncSuccess<LogoutRequest, LogoutResponse> onSuccess,
			AsyncFailure<LogoutRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodLogout, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodLogout, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this, UserMethodLogout,
									input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodLogout, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodLogout, input, exception);
		}
		return handle;
	}

	public static final String UserMethodChangePassword = "ChangePassword";

	public Request changePassword (ChangePasswordRequest input) {
		return changePassword(input, null, null);
	}

	public Request changePassword (ChangePasswordRequest input,
			AsyncSuccess<ChangePasswordRequest, ChangePasswordResponse> onSuccess) {
		return changePassword(input, onSuccess, null);
	}

	public Request changePassword (ChangePasswordRequest input,
			final AsyncCallback<ChangePasswordResponse> callback) {
		return changePassword(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request changePassword (ChangePasswordRequest input,
			AsyncSuccess<ChangePasswordRequest, ChangePasswordResponse> onSuccess,
			AsyncFailure<ChangePasswordRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodChangePassword, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodChangePassword, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodChangePassword, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodChangePassword, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodChangePassword, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodCheckUsername = "CheckUsername";

	public Request checkUsername (CheckUsernameRequest input) {
		return checkUsername(input, null, null);
	}

	public Request checkUsername (CheckUsernameRequest input,
			AsyncSuccess<CheckUsernameRequest, CheckUsernameResponse> onSuccess) {
		return checkUsername(input, onSuccess, null);
	}

	public Request checkUsername (CheckUsernameRequest input,
			final AsyncCallback<CheckUsernameResponse> callback) {
		return checkUsername(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request checkUsername (CheckUsernameRequest input,
			AsyncSuccess<CheckUsernameRequest, CheckUsernameResponse> onSuccess,
			AsyncFailure<CheckUsernameRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodCheckUsername, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodCheckUsername, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodCheckUsername, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodCheckUsername, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodCheckUsername, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodIsAuthorised = "IsAuthorised";

	public Request isAuthorised (IsAuthorisedRequest input) {
		return isAuthorised(input, null, null);
	}

	public Request isAuthorised (IsAuthorisedRequest input,
			AsyncSuccess<IsAuthorisedRequest, IsAuthorisedResponse> onSuccess) {
		return isAuthorised(input, onSuccess, null);
	}

	public Request isAuthorised (IsAuthorisedRequest input,
			final AsyncCallback<IsAuthorisedResponse> callback) {
		return isAuthorised(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request isAuthorised (IsAuthorisedRequest input,
			AsyncSuccess<IsAuthorisedRequest, IsAuthorisedResponse> onSuccess,
			AsyncFailure<IsAuthorisedRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodIsAuthorised, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodIsAuthorised, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodIsAuthorised, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodIsAuthorised, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodIsAuthorised, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodGetUserDetails = "GetUserDetails";

	public Request getUserDetails (GetUserDetailsRequest input) {
		return getUserDetails(input, null, null);
	}

	public Request getUserDetails (GetUserDetailsRequest input,
			AsyncSuccess<GetUserDetailsRequest, GetUserDetailsResponse> onSuccess) {
		return getUserDetails(input, onSuccess, null);
	}

	public Request getUserDetails (GetUserDetailsRequest input,
			final AsyncCallback<GetUserDetailsResponse> callback) {
		return getUserDetails(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getUserDetails (GetUserDetailsRequest input,
			AsyncSuccess<GetUserDetailsRequest, GetUserDetailsResponse> onSuccess,
			AsyncFailure<GetUserDetailsRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodGetUserDetails, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodGetUserDetails, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodGetUserDetails, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodGetUserDetails, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodGetUserDetails, input,
					exception);
		}
		return handle;
	}

	public static final String UserMethodForgotPassword = "ForgotPassword";

	public Request forgotPassword (ForgotPasswordRequest input) {
		return forgotPassword(input, null, null);
	}

	public Request forgotPassword (ForgotPasswordRequest input,
			AsyncSuccess<ForgotPasswordRequest, ForgotPasswordResponse> onSuccess) {
		return forgotPassword(input, onSuccess, null);
	}

	public Request forgotPassword (ForgotPasswordRequest input,
			final AsyncCallback<ForgotPasswordResponse> callback) {
		return forgotPassword(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request forgotPassword (ForgotPasswordRequest input,
			AsyncSuccess<ForgotPasswordRequest, ForgotPasswordResponse> onSuccess,
			AsyncFailure<ForgotPasswordRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(UserService.this,
										UserMethodForgotPassword, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(UserService.this,
										UserMethodForgotPassword, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(UserService.this,
									UserMethodForgotPassword, input, exception);
						}
					});
			onCallStart(UserService.this, UserMethodForgotPassword, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(UserService.this, UserMethodForgotPassword, input,
					exception);
		}
		return handle;
	}
}
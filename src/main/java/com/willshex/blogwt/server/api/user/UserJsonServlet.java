//  
//  UserJsonServlet.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.user;

import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.user.action.BlockUsersActionHandler;
import com.willshex.blogwt.server.api.user.action.ChangePasswordActionHandler;
import com.willshex.blogwt.server.api.user.action.ChangeUserAccessActionHandler;
import com.willshex.blogwt.server.api.user.action.ChangeUserDetailsActionHandler;
import com.willshex.blogwt.server.api.user.action.CheckUsernameActionHandler;
import com.willshex.blogwt.server.api.user.action.FollowUsersActionHandler;
import com.willshex.blogwt.server.api.user.action.ForgotPasswordActionHandler;
import com.willshex.blogwt.server.api.user.action.GetEmailAvatarActionHandler;
import com.willshex.blogwt.server.api.user.action.GetPermissionsActionHandler;
import com.willshex.blogwt.server.api.user.action.GetRolesActionHandler;
import com.willshex.blogwt.server.api.user.action.GetRolesAndPermissionsActionHandler;
import com.willshex.blogwt.server.api.user.action.GetUserDetailsActionHandler;
import com.willshex.blogwt.server.api.user.action.GetUsersActionHandler;
import com.willshex.blogwt.server.api.user.action.IsAuthorisedActionHandler;
import com.willshex.blogwt.server.api.user.action.LoginActionHandler;
import com.willshex.blogwt.server.api.user.action.LogoutActionHandler;
import com.willshex.blogwt.server.api.user.action.RegisterUserActionHandler;
import com.willshex.blogwt.server.api.user.action.ResetPasswordActionHandler;
import com.willshex.blogwt.server.api.user.action.VerifyAccountActionHandler;
import com.willshex.blogwt.shared.api.user.call.BlockUsersRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.FollowUsersRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedRequest;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@SuppressWarnings("serial")
@WebServlet(name = "User API", urlPatterns = "/user")
public final class UserJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		if ("GetUsers".equals(action)) {
			GetUsersRequest input = new GetUsersRequest();
			input.fromJson(request);
			output = new GetUsersActionHandler().handle(input).toString();
		} else if ("BlockUsers".equals(action)) {
			BlockUsersRequest input = new BlockUsersRequest();
			input.fromJson(request);
			output = new BlockUsersActionHandler().handle(input).toString();
		} else if ("FollowUsers".equals(action)) {
			FollowUsersRequest input = new FollowUsersRequest();
			input.fromJson(request);
			output = new FollowUsersActionHandler().handle(input).toString();
		} else if ("ChangeUserAccess".equals(action)) {
			ChangeUserAccessRequest input = new ChangeUserAccessRequest();
			input.fromJson(request);
			output = new ChangeUserAccessActionHandler().handle(input)
					.toString();
		} else if ("GetRoles".equals(action)) {
			GetRolesRequest input = new GetRolesRequest();
			input.fromJson(request);
			output = new GetRolesActionHandler().handle(input).toString();
		} else if ("GetPermissions".equals(action)) {
			GetPermissionsRequest input = new GetPermissionsRequest();
			input.fromJson(request);
			output = new GetPermissionsActionHandler().handle(input).toString();
		} else if ("GetRolesAndPermissions".equals(action)) {
			GetRolesAndPermissionsRequest input = new GetRolesAndPermissionsRequest();
			input.fromJson(request);
			output = new GetRolesAndPermissionsActionHandler().handle(input)
					.toString();
		} else if ("VerifyAccount".equals(action)) {
			VerifyAccountRequest input = new VerifyAccountRequest();
			input.fromJson(request);
			output = new VerifyAccountActionHandler().handle(input).toString();
		} else if ("ResetPassword".equals(action)) {
			ResetPasswordRequest input = new ResetPasswordRequest();
			input.fromJson(request);
			output = new ResetPasswordActionHandler().handle(input).toString();
		} else if ("GetEmailAvatar".equals(action)) {
			GetEmailAvatarRequest input = new GetEmailAvatarRequest();
			input.fromJson(request);
			output = new GetEmailAvatarActionHandler().handle(input).toString();
		} else if ("RegisterUser".equals(action)) {
			RegisterUserRequest input = new RegisterUserRequest();
			input.fromJson(request);
			output = new RegisterUserActionHandler().handle(input).toString();
		} else if ("ChangeUserDetails".equals(action)) {
			ChangeUserDetailsRequest input = new ChangeUserDetailsRequest();
			input.fromJson(request);
			output = new ChangeUserDetailsActionHandler().handle(input)
					.toString();
		} else if ("Login".equals(action)) {
			LoginRequest input = new LoginRequest();
			input.fromJson(request);
			output = new LoginActionHandler().handle(input).toString();
		} else if ("Logout".equals(action)) {
			LogoutRequest input = new LogoutRequest();
			input.fromJson(request);
			output = new LogoutActionHandler().handle(input).toString();
		} else if ("ChangePassword".equals(action)) {
			ChangePasswordRequest input = new ChangePasswordRequest();
			input.fromJson(request);
			output = new ChangePasswordActionHandler().handle(input).toString();
		} else if ("CheckUsername".equals(action)) {
			CheckUsernameRequest input = new CheckUsernameRequest();
			input.fromJson(request);
			output = new CheckUsernameActionHandler().handle(input).toString();
		} else if ("IsAuthorised".equals(action)) {
			IsAuthorisedRequest input = new IsAuthorisedRequest();
			input.fromJson(request);
			output = new IsAuthorisedActionHandler().handle(input).toString();
		} else if ("GetUserDetails".equals(action)) {
			GetUserDetailsRequest input = new GetUserDetailsRequest();
			input.fromJson(request);
			output = new GetUserDetailsActionHandler().handle(input).toString();
		} else if ("ForgotPassword".equals(action)) {
			ForgotPasswordRequest input = new ForgotPasswordRequest();
			input.fromJson(request);
			output = new ForgotPasswordActionHandler().handle(input).toString();
		}
		return output;
	}
}
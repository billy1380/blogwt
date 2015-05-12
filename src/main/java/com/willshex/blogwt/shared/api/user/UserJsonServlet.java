//  
//  UserJsonServlet.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.shared.api.user;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedRequest;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LogoutRequest;
import com.willshex.gson.json.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class UserJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		UserService service = new UserService();
		if ("GetUsers".equals(action)) {
			GetUsersRequest input = new GetUsersRequest();
			input.fromJson(request);
			output = service.getUsers(input).toString();
		} else if ("Login".equals(action)) {
			LoginRequest input = new LoginRequest();
			input.fromJson(request);
			output = service.login(input).toString();
		} else if ("Logout".equals(action)) {
			LogoutRequest input = new LogoutRequest();
			input.fromJson(request);
			output = service.logout(input).toString();
		} else if ("ChangePassword".equals(action)) {
			ChangePasswordRequest input = new ChangePasswordRequest();
			input.fromJson(request);
			output = service.changePassword(input).toString();
		} else if ("ChangeUserDetails".equals(action)) {
			ChangeUserDetailsRequest input = new ChangeUserDetailsRequest();
			input.fromJson(request);
			output = service.changeUserDetails(input).toString();
		} else if ("CheckUsername".equals(action)) {
			CheckUsernameRequest input = new CheckUsernameRequest();
			input.fromJson(request);
			output = service.checkUsername(input).toString();
		} else if ("GetRolesAndPermissions".equals(action)) {
			GetRolesAndPermissionsRequest input = new GetRolesAndPermissionsRequest();
			input.fromJson(request);
			output = service.getRolesAndPermissions(input).toString();
		} else if ("IsAuthorised".equals(action)) {
			IsAuthorisedRequest input = new IsAuthorisedRequest();
			input.fromJson(request);
			output = service.isAuthorised(input).toString();
		} else if ("GetUserDetails".equals(action)) {
			GetUserDetailsRequest input = new GetUserDetailsRequest();
			input.fromJson(request);
			output = service.getUserDetails(input).toString();
		} else if ("ForgotPassword".equals(action)) {
			ForgotPasswordRequest input = new ForgotPasswordRequest();
			input.fromJson(request);
			output = service.forgotPassword(input).toString();
		}
		return output;
	}
}
//  
//  ResetPasswordRequest.java
//  blogwt
//
//  Created by William Shakour on September 6, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;

public class ResetPasswordRequest extends Request {
	public String email;
	public String username;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonEmail = email == null ? JsonNull.INSTANCE
				: new JsonPrimitive(email);
		object.add("email", jsonEmail);
		JsonElement jsonUsername = username == null ? JsonNull.INSTANCE
				: new JsonPrimitive(username);
		object.add("username", jsonUsername);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("email")) {
			JsonElement jsonEmail = jsonObject.get("email");
			if (jsonEmail != null) {
				email = jsonEmail.getAsString();
			}
		}
		if (jsonObject.has("username")) {
			JsonElement jsonUsername = jsonObject.get("username");
			if (jsonUsername != null) {
				username = jsonUsername.getAsString();
			}
		}
	}

	public ResetPasswordRequest email (String email) {
		this.email = email;
		return this;
	}

	public ResetPasswordRequest username (String username) {
		this.username = username;
		return this;
	}
}
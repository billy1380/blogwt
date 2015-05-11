//  
//  ForgotPasswordRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;

public class ForgotPasswordRequest extends Request {
	public String username;
	public String email;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUsername = username == null ? JsonNull.INSTANCE
				: new JsonPrimitive(username);
		object.add("username", jsonUsername);
		JsonElement jsonEmail = email == null ? JsonNull.INSTANCE
				: new JsonPrimitive(email);
		object.add("email", jsonEmail);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("username")) {
			JsonElement jsonUsername = jsonObject.get("username");
			if (jsonUsername != null) {
				username = jsonUsername.getAsString();
			}
		}
		if (jsonObject.has("email")) {
			JsonElement jsonEmail = jsonObject.get("email");
			if (jsonEmail != null) {
				email = jsonEmail.getAsString();
			}
		}
	}

	public ForgotPasswordRequest username (String username) {
		this.username = username;
		return this;
	}

	public ForgotPasswordRequest email (String email) {
		this.email = email;
		return this;
	}
}
//  
//  LoginRequest.java
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

public class LoginRequest extends Request {
	public String username;
	public String password;
	public Boolean longTerm;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUsername = username == null ? JsonNull.INSTANCE
				: new JsonPrimitive(username);
		object.add("username", jsonUsername);
		JsonElement jsonPassword = password == null ? JsonNull.INSTANCE
				: new JsonPrimitive(password);
		object.add("password", jsonPassword);
		JsonElement jsonLongTerm = longTerm == null ? JsonNull.INSTANCE
				: new JsonPrimitive(longTerm);
		object.add("longTerm", jsonLongTerm);
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
		if (jsonObject.has("password")) {
			JsonElement jsonPassword = jsonObject.get("password");
			if (jsonPassword != null) {
				password = jsonPassword.getAsString();
			}
		}
		if (jsonObject.has("longTerm")) {
			JsonElement jsonLongTerm = jsonObject.get("longTerm");
			if (jsonLongTerm != null) {
				longTerm = Boolean.valueOf(jsonLongTerm.getAsBoolean());
			}
		}
	}

	public LoginRequest username (String username) {
		this.username = username;
		return this;
	}

	public LoginRequest password (String password) {
		this.password = password;
		return this;
	}

	public LoginRequest longTerm (Boolean longTerm) {
		this.longTerm = longTerm;
		return this;
	}
}
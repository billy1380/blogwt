//  
//  ChangePasswordRequest.java
//  blogwt
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

public class ChangePasswordRequest extends Request {
	public String resetCode;
	public String password;
	public String changedPassword;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonResetCode = resetCode == null ? JsonNull.INSTANCE
				: new JsonPrimitive(resetCode);
		object.add("resetCode", jsonResetCode);
		JsonElement jsonPassword = password == null ? JsonNull.INSTANCE
				: new JsonPrimitive(password);
		object.add("password", jsonPassword);
		JsonElement jsonChangedPassword = changedPassword == null ? JsonNull.INSTANCE
				: new JsonPrimitive(changedPassword);
		object.add("changedPassword", jsonChangedPassword);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("resetCode")) {
			JsonElement jsonResetCode = jsonObject.get("resetCode");
			if (jsonResetCode != null) {
				resetCode = jsonResetCode.getAsString();
			}
		}
		if (jsonObject.has("password")) {
			JsonElement jsonPassword = jsonObject.get("password");
			if (jsonPassword != null) {
				password = jsonPassword.getAsString();
			}
		}
		if (jsonObject.has("changedPassword")) {
			JsonElement jsonChangedPassword = jsonObject.get("changedPassword");
			if (jsonChangedPassword != null) {
				changedPassword = jsonChangedPassword.getAsString();
			}
		}
	}

	public ChangePasswordRequest resetCode (String resetCode) {
		this.resetCode = resetCode;
		return this;
	}

	public ChangePasswordRequest password (String password) {
		this.password = password;
		return this;
	}

	public ChangePasswordRequest changedPassword (String changedPassword) {
		this.changedPassword = changedPassword;
		return this;
	}
}
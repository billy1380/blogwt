//  
//  CheckUsernameResponse.java
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
import com.willshex.blogwt.shared.api.Response;

public class CheckUsernameResponse extends Response {
	public Boolean usernameInUse;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUsernameInUse = usernameInUse == null ? JsonNull.INSTANCE
				: new JsonPrimitive(usernameInUse);
		object.add("usernameInUse", jsonUsernameInUse);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("usernameInUse")) {
			JsonElement jsonUsernameInUse = jsonObject.get("usernameInUse");
			if (jsonUsernameInUse != null) {
				usernameInUse = Boolean.valueOf(jsonUsernameInUse
						.getAsBoolean());
			}
		}
	}

	public CheckUsernameResponse usernameInUse (Boolean usernameInUse) {
		this.usernameInUse = usernameInUse;
		return this;
	}
}
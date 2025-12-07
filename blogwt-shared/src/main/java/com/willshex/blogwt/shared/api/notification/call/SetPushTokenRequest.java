// 
//  SetPushTokenRequest.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.notification.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.PushToken;

public class SetPushTokenRequest extends Request {
	public PushToken token;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonToken = token == null ? JsonNull.INSTANCE
				: token.toJson();
		object.add("token", jsonToken);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("token")) {
			JsonElement jsonToken = jsonObject.get("token");
			if (jsonToken != null) {
				token = new PushToken();
				token.fromJson(jsonToken.getAsJsonObject());
			}
		}
	}

	public SetPushTokenRequest token (PushToken token) {
		this.token = token;
		return this;
	}
}
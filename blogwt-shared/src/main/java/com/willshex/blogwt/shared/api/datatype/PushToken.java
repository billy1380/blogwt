// 
//  PushToken.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class PushToken extends DataType {
	@Index public Key<User> userKey;
	@Ignore public User user;

	public String value;

	@Index public String platform;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonValue = value == null ? JsonNull.INSTANCE
				: new JsonPrimitive(value);
		object.add("value", jsonValue);
		JsonElement jsonPlatform = platform == null ? JsonNull.INSTANCE
				: new JsonPrimitive(platform);
		object.add("platform", jsonPlatform);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
			}
		}
		if (jsonObject.has("value")) {
			JsonElement jsonValue = jsonObject.get("value");
			if (jsonValue != null) {
				value = jsonValue.getAsString();
			}
		}
		if (jsonObject.has("platform")) {
			JsonElement jsonPlatform = jsonObject.get("platform");
			if (jsonPlatform != null) {
				platform = jsonPlatform.getAsString();
			}
		}
	}

	public PushToken user (User user) {
		this.user = user;
		return this;
	}

	public PushToken value (String value) {
		this.value = value;
		return this;
	}

	public PushToken platform (String platform) {
		this.platform = platform;
		return this;
	}
}
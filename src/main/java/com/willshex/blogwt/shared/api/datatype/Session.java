//  
//  Session.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.Date;

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
public class Session extends DataType {
	public Date expires;
	@Index public Key<User> userKey;
	@Ignore public User user;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonExpires = expires == null ? JsonNull.INSTANCE : new JsonPrimitive(expires.getTime());
		object.add("expires", jsonExpires);
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("expires")) {
			JsonElement jsonExpires = jsonObject.get("expires");
			if (jsonExpires != null) {
				expires = new Date(jsonExpires.getAsLong());
			}
		}
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
			}
		}
	}

	public Session expires(Date expires) {
		this.expires = expires;
		return this;
	}

	public Session user(User user) {
		this.user = user;
		return this;
	}
}
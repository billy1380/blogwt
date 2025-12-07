//  
//  Request.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.datatype.Session;

public class Request extends com.willshex.gson.web.service.shared.Request {
	public Session session;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonAccessCode = accessCode == null ? JsonNull.INSTANCE : new JsonPrimitive(accessCode);
		object.add("accessCode", jsonAccessCode);
		JsonElement jsonSession = session == null ? JsonNull.INSTANCE : session.toJson();
		object.add("session", jsonSession);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("accessCode")) {
			JsonElement jsonAccessCode = jsonObject.get("accessCode");
			if (jsonAccessCode != null) {
				accessCode = jsonAccessCode.getAsString();
			}
		}
		if (jsonObject.has("session")) {
			JsonElement jsonSession = jsonObject.get("session");
			if (jsonSession != null) {
				session = new Session();
				session.fromJson(jsonSession.getAsJsonObject());
			}
		}
	}

	public Request accessCode(String accessCode) {
		this.accessCode = accessCode;
		return this;
	}

	public Request session(Session session) {
		this.session = session;
		return this;
	}
}
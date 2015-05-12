//  
//  Response.java
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
import com.willshex.gson.json.service.shared.Error;
import com.willshex.gson.json.service.shared.StatusType;

public class Response extends com.willshex.gson.json.service.shared.Response {
	public Session session;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonStatus = status == null ? JsonNull.INSTANCE : new JsonPrimitive(status.toString());
		object.add("status", jsonStatus);
		JsonElement jsonError = error == null ? JsonNull.INSTANCE : error.toJson();
		object.add("error", jsonError);
		JsonElement jsonSession = session == null ? JsonNull.INSTANCE : session.toJson();
		object.add("session", jsonSession);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("status")) {
			JsonElement jsonStatus = jsonObject.get("status");
			if (jsonStatus != null) {
				status = StatusType.fromString(jsonStatus.getAsString());
			}
		}
		if (jsonObject.has("error")) {
			JsonElement jsonError = jsonObject.get("error");
			if (jsonError != null) {
				error = new Error();
				error.fromJson(jsonError.getAsJsonObject());
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

	public Response status(StatusType status) {
		this.status = status;
		return this;
	}

	public Response error(Error error) {
		this.error = error;
		return this;
	}

	public Response session(Session session) {
		this.session = session;
		return this;
	}
}
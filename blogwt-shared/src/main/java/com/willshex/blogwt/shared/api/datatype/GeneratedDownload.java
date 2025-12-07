// 
//  GeneratedDownload.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
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
public class GeneratedDownload extends DataType {
	public String parameters;

	@Ignore public User user;
	@Index public Key<User> userKey;

	public GeneratedDownloadStatusType status;
	public String url;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonParameters = parameters == null ? JsonNull.INSTANCE
				: new JsonPrimitive(parameters);
		object.add("parameters", jsonParameters);
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonStatus = status == null ? JsonNull.INSTANCE
				: new JsonPrimitive(status.toString());
		object.add("status", jsonStatus);
		JsonElement jsonUrl = url == null ? JsonNull.INSTANCE
				: new JsonPrimitive(url);
		object.add("url", jsonUrl);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("parameters")) {
			JsonElement jsonParameters = jsonObject.get("parameters");
			if (jsonParameters != null) {
				parameters = jsonParameters.getAsString();
			}
		}
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
			}
		}
		if (jsonObject.has("status")) {
			JsonElement jsonStatus = jsonObject.get("status");
			if (jsonStatus != null) {
				status = GeneratedDownloadStatusType
						.fromString(jsonStatus.getAsString());
			}
		}
		if (jsonObject.has("url")) {
			JsonElement jsonUrl = jsonObject.get("url");
			if (jsonUrl != null) {
				url = jsonUrl.getAsString();
			}
		}
	}

	public GeneratedDownload parameters (String parameters) {
		this.parameters = parameters;
		return this;
	}

	public GeneratedDownload user (User user) {
		this.user = user;
		return this;
	}

	public GeneratedDownload status (GeneratedDownloadStatusType status) {
		this.status = status;
		return this;
	}

	public GeneratedDownload url (String url) {
		this.url = url;
		return this;
	}
}
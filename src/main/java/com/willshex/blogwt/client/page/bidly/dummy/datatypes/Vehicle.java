// 
//  Vehicle.java
//  bidly
// 
//  Created by William Shakour on December 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.page.bidly.dummy.datatypes;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.datatype.DataType;

public class Vehicle extends DataType {
	public Build build;
	public String registration;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonBuild = build == null ? JsonNull.INSTANCE
				: build.toJson();
		object.add("build", jsonBuild);
		JsonElement jsonRegistration = registration == null ? JsonNull.INSTANCE
				: new JsonPrimitive(registration);
		object.add("registration", jsonRegistration);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("build")) {
			JsonElement jsonBuild = jsonObject.get("build");
			if (jsonBuild != null) {
				build = new Build();
				build.fromJson(jsonBuild.getAsJsonObject());
			}
		}
		if (jsonObject.has("registration")) {
			JsonElement jsonRegistration = jsonObject.get("registration");
			if (jsonRegistration != null) {
				registration = jsonRegistration.getAsString();
			}
		}
	}

	public Vehicle build (Build build) {
		this.build = build;
		return this;
	}

	public Vehicle registration (String registration) {
		this.registration = registration;
		return this;
	}
}
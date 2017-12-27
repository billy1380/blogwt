// 
//  Build.java
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

public class Build extends DataType {
	public String make;
	public String model;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonMake = make == null ? JsonNull.INSTANCE
				: new JsonPrimitive(make);
		object.add("make", jsonMake);
		JsonElement jsonModel = model == null ? JsonNull.INSTANCE
				: new JsonPrimitive(model);
		object.add("model", jsonModel);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("make")) {
			JsonElement jsonMake = jsonObject.get("make");
			if (jsonMake != null) {
				make = jsonMake.getAsString();
			}
		}
		if (jsonObject.has("model")) {
			JsonElement jsonModel = jsonObject.get("model");
			if (jsonModel != null) {
				model = jsonModel.getAsString();
			}
		}
	}

	public Build make (String make) {
		this.make = make;
		return this;
	}

	public Build model (String model) {
		this.model = model;
		return this;
	}
}
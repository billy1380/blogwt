//  
//  Field.java
//  xsdwsdl2code
//
//  Created by William Shakour on November 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Field extends DataType {
	public String name;
	public String value;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonValue = value == null ? JsonNull.INSTANCE
				: new JsonPrimitive(value);
		object.add("value", jsonValue);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
		if (jsonObject.has("value")) {
			JsonElement jsonValue = jsonObject.get("value");
			if (jsonValue != null) {
				value = jsonValue.getAsString();
			}
		}
	}

	public Field name (String name) {
		this.name = name;
		return this;
	}

	public Field value (String value) {
		this.value = value;
		return this;
	}
}
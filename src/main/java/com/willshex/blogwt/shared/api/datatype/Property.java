//  
//  Property.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Property extends DataType {
	@Index public String name;
	public String description;
	@Index public String value;
	public String group;
	public String type;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE
				: new JsonPrimitive(description);
		object.add("description", jsonDescription);
		JsonElement jsonValue = value == null ? JsonNull.INSTANCE
				: new JsonPrimitive(value);
		object.add("value", jsonValue);
		JsonElement jsonGroup = group == null ? JsonNull.INSTANCE
				: new JsonPrimitive(group);
		object.add("group", jsonGroup);
		JsonElement jsonType = type == null ? JsonNull.INSTANCE
				: new JsonPrimitive(type);
		object.add("type", jsonType);
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
		if (jsonObject.has("description")) {
			JsonElement jsonDescription = jsonObject.get("description");
			if (jsonDescription != null) {
				description = jsonDescription.getAsString();
			}
		}
		if (jsonObject.has("value")) {
			JsonElement jsonValue = jsonObject.get("value");
			if (jsonValue != null) {
				value = jsonValue.getAsString();
			}
		}
		if (jsonObject.has("group")) {
			JsonElement jsonGroup = jsonObject.get("group");
			if (jsonGroup != null) {
				group = jsonGroup.getAsString();
			}
		}
		if (jsonObject.has("type")) {
			JsonElement jsonType = jsonObject.get("type");
			if (jsonType != null) {
				type = jsonType.getAsString();
			}
		}
	}

	public Property name (String name) {
		this.name = name;
		return this;
	}

	public Property description (String description) {
		this.description = description;
		return this;
	}

	public Property value (String value) {
		this.value = value;
		return this;
	}

	public Property group (String group) {
		this.group = group;
		return this;
	}

	public Property type (String type) {
		this.type = type;
		return this;
	}
}
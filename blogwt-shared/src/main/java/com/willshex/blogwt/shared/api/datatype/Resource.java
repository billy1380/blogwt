//  
//  Resource.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
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
import com.googlecode.objectify.condition.IfNotEmpty;

@Entity
@Cache
public class Resource extends DataType {
	@Index(value = IfNotEmpty.class) public String name;
	public String data;
	public String description;
	public ResourceTypeType type;
	public String properties;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonData = data == null ? JsonNull.INSTANCE
				: new JsonPrimitive(data);
		object.add("data", jsonData);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE
				: new JsonPrimitive(description);
		object.add("description", jsonDescription);
		JsonElement jsonType = type == null ? JsonNull.INSTANCE
				: new JsonPrimitive(type.toString());
		object.add("type", jsonType);
		JsonElement jsonProperties = properties == null ? JsonNull.INSTANCE
				: new JsonPrimitive(properties);
		object.add("properties", jsonProperties);
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
		if (jsonObject.has("data")) {
			JsonElement jsonData = jsonObject.get("data");
			if (jsonData != null) {
				data = jsonData.getAsString();
			}
		}
		if (jsonObject.has("description")) {
			JsonElement jsonDescription = jsonObject.get("description");
			if (jsonDescription != null) {
				description = jsonDescription.getAsString();
			}
		}
		if (jsonObject.has("type")) {
			JsonElement jsonType = jsonObject.get("type");
			if (jsonType != null) {
				type = ResourceTypeType.fromString(jsonType.getAsString());
			}
		}
		if (jsonObject.has("properties")) {
			JsonElement jsonProperties = jsonObject.get("properties");
			if (jsonProperties != null) {
				properties = jsonProperties.getAsString();
			}
		}
	}

	public Resource name (String name) {
		this.name = name;
		return this;
	}

	public Resource data (String data) {
		this.data = data;
		return this;
	}

	public Resource description (String description) {
		this.description = description;
		return this;
	}

	public Resource type (ResourceTypeType type) {
		this.type = type;
		return this;
	}

	public Resource properties (String properties) {
		this.properties = properties;
		return this;
	}
}
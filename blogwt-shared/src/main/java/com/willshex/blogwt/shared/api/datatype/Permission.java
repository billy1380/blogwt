//  
//  Permission.java
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

@Entity
@Cache
public class Permission extends DataType {
	@Index public String code;
	public PermissionTypeType type;
	public String name;
	public String description;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonCode = code == null ? JsonNull.INSTANCE : new JsonPrimitive(code);
		object.add("code", jsonCode);
		JsonElement jsonType = type == null ? JsonNull.INSTANCE : new JsonPrimitive(type.toString());
		object.add("type", jsonType);
		JsonElement jsonName = name == null ? JsonNull.INSTANCE : new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE : new JsonPrimitive(description);
		object.add("description", jsonDescription);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("code")) {
			JsonElement jsonCode = jsonObject.get("code");
			if (jsonCode != null) {
				code = jsonCode.getAsString();
			}
		}
		if (jsonObject.has("type")) {
			JsonElement jsonType = jsonObject.get("type");
			if (jsonType != null) {
				type = PermissionTypeType.fromString(jsonType.getAsString());
			}
		}
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
	}

	public Permission code(String code) {
		this.code = code;
		return this;
	}

	public Permission type(PermissionTypeType type) {
		this.type = type;
		return this;
	}

	public Permission name(String name) {
		this.name = name;
		return this;
	}

	public Permission description(String description) {
		this.description = description;
		return this;
	}
}
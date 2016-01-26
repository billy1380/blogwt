//  
//  MetaNotification.java
//  xsdwsdl2code
//
//  Created by William Shakour on January 26, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
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
public class MetaNotification extends DataType {
	@Index public String code;
	public String content;
	public String name;
	public String description;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonCode = code == null ? JsonNull.INSTANCE
				: new JsonPrimitive(code);
		object.add("code", jsonCode);
		JsonElement jsonContent = content == null ? JsonNull.INSTANCE
				: new JsonPrimitive(content);
		object.add("content", jsonContent);
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE
				: new JsonPrimitive(description);
		object.add("description", jsonDescription);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("code")) {
			JsonElement jsonCode = jsonObject.get("code");
			if (jsonCode != null) {
				code = jsonCode.getAsString();
			}
		}
		if (jsonObject.has("content")) {
			JsonElement jsonContent = jsonObject.get("content");
			if (jsonContent != null) {
				content = jsonContent.getAsString();
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

	public MetaNotification code (String code) {
		this.code = code;
		return this;
	}

	public MetaNotification content (String content) {
		this.content = content;
		return this;
	}

	public MetaNotification name (String name) {
		this.name = name;
		return this;
	}

	public MetaNotification description (String description) {
		this.description = description;
		return this;
	}
}
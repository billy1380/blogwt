//  
//  MetaNotification.java
//  blogwt
//
//  Created by William Shakour on January 26, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
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
	public String group;
	public List<NotificationModeType> modes;
	public List<NotificationModeType> defaults;

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
		JsonElement jsonGroup = group == null ? JsonNull.INSTANCE
				: new JsonPrimitive(group);
		object.add("group", jsonGroup);
		JsonElement jsonModes = JsonNull.INSTANCE;
		if (modes != null) {
			jsonModes = new JsonArray();
			for (int i = 0; i < modes.size(); i++) {
				JsonElement jsonModesItem = modes.get(i) == null
						? JsonNull.INSTANCE
						: new JsonPrimitive(modes.get(i).toString());
				((JsonArray) jsonModes).add(jsonModesItem);
			}
		}
		object.add("modes", jsonModes);
		JsonElement jsonDefaults = JsonNull.INSTANCE;
		if (defaults != null) {
			jsonDefaults = new JsonArray();
			for (int i = 0; i < defaults.size(); i++) {
				JsonElement jsonDefaultsItem = defaults.get(i) == null
						? JsonNull.INSTANCE
						: new JsonPrimitive(defaults.get(i).toString());
				((JsonArray) jsonDefaults).add(jsonDefaultsItem);
			}
		}
		object.add("defaults", jsonDefaults);
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
		if (jsonObject.has("group")) {
			JsonElement jsonGroup = jsonObject.get("group");
			if (jsonGroup != null) {
				group = jsonGroup.getAsString();
			}
		}
		if (jsonObject.has("modes")) {
			JsonElement jsonModes = jsonObject.get("modes");
			if (jsonModes != null) {
				modes = new ArrayList<NotificationModeType>();
				for (int i = 0; i < jsonModes.getAsJsonArray().size(); i++) {
					modes.add(NotificationModeType.fromString(
							jsonModes.getAsJsonArray().get(i).getAsString()));
				}
			}
		}

		if (jsonObject.has("defaults")) {
			JsonElement jsonDefaults = jsonObject.get("defaults");
			if (jsonDefaults != null) {
				defaults = new ArrayList<NotificationModeType>();
				for (int i = 0; i < jsonDefaults.getAsJsonArray().size(); i++) {
					defaults.add(NotificationModeType.fromString(jsonDefaults
							.getAsJsonArray().get(i).getAsString()));
				}
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

	public MetaNotification group (String group) {
		this.group = group;
		return this;
	}

	public MetaNotification modes (List<NotificationModeType> modes) {
		this.modes = modes;
		return this;
	}

	public MetaNotification defaults (List<NotificationModeType> defaults) {
		this.defaults = defaults;
		return this;
	}
}
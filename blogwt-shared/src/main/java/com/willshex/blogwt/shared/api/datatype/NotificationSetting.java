// 
//  NotificationSetting.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
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
public class NotificationSetting extends DataType {
	@Index public Key<User> userKey;
	@Ignore public User user;

	@Index public Key<MetaNotification> metaKey;
	@Ignore public MetaNotification meta;

	public List<NotificationModeType> selected;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonMeta = meta == null ? JsonNull.INSTANCE : meta.toJson();
		object.add("meta", jsonMeta);
		JsonElement jsonSelected = JsonNull.INSTANCE;
		if (selected != null) {
			jsonSelected = new JsonArray();
			for (int i = 0; i < selected.size(); i++) {
				JsonElement jsonSelectedItem = selected.get(i) == null
						? JsonNull.INSTANCE
						: new JsonPrimitive(selected.get(i).toString());
				((JsonArray) jsonSelected).add(jsonSelectedItem);
			}
		}
		object.add("selected", jsonSelected);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
			}
		}
		if (jsonObject.has("meta")) {
			JsonElement jsonMeta = jsonObject.get("meta");
			if (jsonMeta != null) {
				meta = new MetaNotification();
				meta.fromJson(jsonMeta.getAsJsonObject());
			}
		}
		if (jsonObject.has("selected")) {
			JsonElement jsonSelected = jsonObject.get("selected");
			if (jsonSelected != null) {
				selected = new ArrayList<NotificationModeType>();
				for (int i = 0; i < jsonSelected.getAsJsonArray().size(); i++) {
					selected.add(NotificationModeType.fromString(jsonSelected
							.getAsJsonArray().get(i).getAsString()));
				}
			}
		}
	}

	public NotificationSetting user (User user) {
		this.user = user;
		return this;
	}

	public NotificationSetting meta (MetaNotification meta) {
		this.meta = meta;
		return this;
	}

	public NotificationSetting selected (List<NotificationModeType> selected) {
		this.selected = selected;
		return this;
	}
}
//  
//  Notification.java
//  blogwt
//
//  Created by William Shakour on January 26, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
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
public class Notification extends DataType {
	public Key<MetaNotification> metaKey;
	@Ignore public MetaNotification meta;

	@Index public Key<User> targetKey;
	@Ignore public User target;

	public Key<User> senderKey;
	@Ignore public User sender;

	public String content;
	@Index public NotificationModeType mode;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonMeta = meta == null ? JsonNull.INSTANCE : meta.toJson();
		object.add("meta", jsonMeta);
		JsonElement jsonTarget = target == null ? JsonNull.INSTANCE
				: target.toJson();
		object.add("target", jsonTarget);
		JsonElement jsonSender = sender == null ? JsonNull.INSTANCE
				: sender.toJson();
		object.add("sender", jsonSender);
		JsonElement jsonContent = content == null ? JsonNull.INSTANCE
				: new JsonPrimitive(content);
		object.add("content", jsonContent);
		JsonElement jsonMode = mode == null ? JsonNull.INSTANCE
				: new JsonPrimitive(mode.toString());
		object.add("mode", jsonMode);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("meta")) {
			JsonElement jsonMeta = jsonObject.get("meta");
			if (jsonMeta != null) {
				meta = new MetaNotification();
				meta.fromJson(jsonMeta.getAsJsonObject());
			}
		}
		if (jsonObject.has("target")) {
			JsonElement jsonTarget = jsonObject.get("target");
			if (jsonTarget != null) {
				target = new User();
				target.fromJson(jsonTarget.getAsJsonObject());
			}
		}
		if (jsonObject.has("sender")) {
			JsonElement jsonSender = jsonObject.get("sender");
			if (jsonSender != null) {
				sender = new User();
				sender.fromJson(jsonSender.getAsJsonObject());
			}
		}
		if (jsonObject.has("content")) {
			JsonElement jsonContent = jsonObject.get("content");
			if (jsonContent != null) {
				content = jsonContent.getAsString();
			}
		}
		if (jsonObject.has("mode")) {
			JsonElement jsonMode = jsonObject.get("mode");
			if (jsonMode != null) {
				mode = NotificationModeType.fromString(jsonMode.getAsString());
			}
		}
	}

	public Notification meta (MetaNotification meta) {
		this.meta = meta;
		return this;
	}

	public Notification target (User target) {
		this.target = target;
		return this;
	}

	public Notification sender (User sender) {
		this.sender = sender;
		return this;
	}

	public Notification content (String content) {
		this.content = content;
		return this;
	}

	public Notification mode (NotificationModeType mode) {
		this.mode = mode;
		return this;
	}
}
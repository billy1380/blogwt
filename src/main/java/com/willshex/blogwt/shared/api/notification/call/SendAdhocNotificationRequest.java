// 
//  SendAdhocNotificationRequest.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.notification.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;
import com.willshex.blogwt.shared.api.datatype.User;

public class SendAdhocNotificationRequest extends Request {
	public String content;
	public List<User> users;
	public NotificationModeType mode;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonContent = content == null ? JsonNull.INSTANCE
				: new JsonPrimitive(content);
		object.add("content", jsonContent);
		JsonElement jsonUsers = JsonNull.INSTANCE;
		if (users != null) {
			jsonUsers = new JsonArray();
			for (int i = 0; i < users.size(); i++) {
				JsonElement jsonUsersItem = users.get(i) == null
						? JsonNull.INSTANCE : users.get(i).toJson();
				((JsonArray) jsonUsers).add(jsonUsersItem);
			}
		}
		object.add("users", jsonUsers);
		JsonElement jsonMode = mode == null ? JsonNull.INSTANCE
				: new JsonPrimitive(mode.toString());
		object.add("mode", jsonMode);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("content")) {
			JsonElement jsonContent = jsonObject.get("content");
			if (jsonContent != null) {
				content = jsonContent.getAsString();
			}
		}
		if (jsonObject.has("users")) {
			JsonElement jsonUsers = jsonObject.get("users");
			if (jsonUsers != null) {
				users = new ArrayList<User>();
				User item = null;
				for (int i = 0; i < jsonUsers.getAsJsonArray().size(); i++) {
					if (jsonUsers.getAsJsonArray().get(i) != null) {
						(item = new User()).fromJson(jsonUsers.getAsJsonArray()
								.get(i).getAsJsonObject());
						users.add(item);
					}
				}
			}
		}

		if (jsonObject.has("mode")) {
			JsonElement jsonMode = jsonObject.get("mode");
			if (jsonMode != null) {
				mode = NotificationModeType.fromString(jsonMode.getAsString());
			}
		}
	}

	public SendAdhocNotificationRequest content (String content) {
		this.content = content;
		return this;
	}

	public SendAdhocNotificationRequest users (List<User> users) {
		this.users = users;
		return this;
	}

	public SendAdhocNotificationRequest mode (NotificationModeType mode) {
		this.mode = mode;
		return this;
	}
}
// 
//  UpdateNotificationSettingsResponse.java
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
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;

public class UpdateNotificationSettingsResponse extends Response {
	public List<NotificationSetting> settings;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonSettings = JsonNull.INSTANCE;
		if (settings != null) {
			jsonSettings = new JsonArray();
			for (int i = 0; i < settings.size(); i++) {
				JsonElement jsonSettingsItem = settings.get(i) == null
						? JsonNull.INSTANCE : settings.get(i).toJson();
				((JsonArray) jsonSettings).add(jsonSettingsItem);
			}
		}
		object.add("settings", jsonSettings);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("settings")) {
			JsonElement jsonSettings = jsonObject.get("settings");
			if (jsonSettings != null) {
				settings = new ArrayList<NotificationSetting>();
				NotificationSetting item = null;
				for (int i = 0; i < jsonSettings.getAsJsonArray().size(); i++) {
					if (jsonSettings.getAsJsonArray().get(i) != null) {
						(item = new NotificationSetting()).fromJson(jsonSettings
								.getAsJsonArray().get(i).getAsJsonObject());
						settings.add(item);
					}
				}
			}
		}

	}

	public UpdateNotificationSettingsResponse settings (
			List<NotificationSetting> settings) {
		this.settings = settings;
		return this;
	}
}
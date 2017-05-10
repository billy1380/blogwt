// 
//  GetNotificationsResponse.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.notification.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Notification;

public class GetNotificationsResponse extends Response {
	public Notification notifications;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonNotifications = notifications == null
				? JsonNull.INSTANCE : notifications.toJson();
		object.add("notifications", jsonNotifications);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("notifications")) {
			JsonElement jsonNotifications = jsonObject.get("notifications");
			if (jsonNotifications != null) {
				notifications = new Notification();
				notifications.fromJson(jsonNotifications.getAsJsonObject());
			}
		}
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetNotificationsResponse notifications (Notification notifications) {
		this.notifications = notifications;
		return this;
	}

	public GetNotificationsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
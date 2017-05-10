// 
//  GetMetaNotificationsResponse.java
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
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;

public class GetMetaNotificationsResponse extends Response {
	public List<MetaNotification> metaNotifications;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonMetaNotifications = JsonNull.INSTANCE;
		if (metaNotifications != null) {
			jsonMetaNotifications = new JsonArray();
			for (int i = 0; i < metaNotifications.size(); i++) {
				JsonElement jsonMetaNotificationsItem = metaNotifications
						.get(i) == null ? JsonNull.INSTANCE
								: metaNotifications.get(i).toJson();
				((JsonArray) jsonMetaNotifications)
						.add(jsonMetaNotificationsItem);
			}
		}
		object.add("metaNotifications", jsonMetaNotifications);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("metaNotifications")) {
			JsonElement jsonMetaNotifications = jsonObject
					.get("metaNotifications");
			if (jsonMetaNotifications != null) {
				metaNotifications = new ArrayList<MetaNotification>();
				MetaNotification item = null;
				for (int i = 0; i < jsonMetaNotifications.getAsJsonArray()
						.size(); i++) {
					if (jsonMetaNotifications.getAsJsonArray().get(i) != null) {
						(item = new MetaNotification())
								.fromJson(jsonMetaNotifications.getAsJsonArray()
										.get(i).getAsJsonObject());
						metaNotifications.add(item);
					}
				}
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

	public GetMetaNotificationsResponse metaNotifications (
			List<MetaNotification> metaNotifications) {
		this.metaNotifications = metaNotifications;
		return this;
	}

	public GetMetaNotificationsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
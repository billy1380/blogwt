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
	public List<MetaNotification> metas;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonMetas = JsonNull.INSTANCE;
		if (metas != null) {
			jsonMetas = new JsonArray();
			for (int i = 0; i < metas.size(); i++) {
				JsonElement jsonMetasItem = metas.get(i) == null
						? JsonNull.INSTANCE : metas.get(i).toJson();
				((JsonArray) jsonMetas).add(jsonMetasItem);
			}
		}
		object.add("metas", jsonMetas);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("metas")) {
			JsonElement jsonMetas = jsonObject.get("metas");
			if (jsonMetas != null) {
				metas = new ArrayList<MetaNotification>();
				MetaNotification item = null;
				for (int i = 0; i < jsonMetas.getAsJsonArray().size(); i++) {
					if (jsonMetas.getAsJsonArray().get(i) != null) {
						(item = new MetaNotification()).fromJson(jsonMetas
								.getAsJsonArray().get(i).getAsJsonObject());
						metas.add(item);
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

	public GetMetaNotificationsResponse metas (List<MetaNotification> metas) {
		this.metas = metas;
		return this;
	}

	public GetMetaNotificationsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
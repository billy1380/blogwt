// 
//  GetMetaNotificationRequest.java
//  blogwt
// 
//  Created by William Shakour on March 19, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.notification.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;

public class GetMetaNotificationRequest extends Request {
	public MetaNotification meta;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonMeta = meta == null ? JsonNull.INSTANCE : meta.toJson();
		object.add("meta", jsonMeta);
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
	}

	/**
	  * Fluent setter for meta.
	  * 
	  * @param parameters
	  * @return
	  */
	public GetMetaNotificationRequest meta (MetaNotification meta) {
		this.meta = meta;
		return this;
	}
}
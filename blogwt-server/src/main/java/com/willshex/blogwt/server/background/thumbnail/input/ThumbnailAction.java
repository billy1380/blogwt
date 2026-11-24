//
//  ThumbnailAction.java
//  blogwt
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.background.thumbnail.input;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.gson.web.service.shared.Request;

/** A message for the thumbnail queue: the resource to make or remove a thumbnail of. */
public class ThumbnailAction extends Request {
	public Long resourceId;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonResourceId = resourceId == null ? JsonNull.INSTANCE
				: new JsonPrimitive(resourceId);
		object.add("resourceId", jsonResourceId);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("resourceId")) {
			JsonElement jsonResourceId = jsonObject.get("resourceId");
			if (jsonResourceId != null && !jsonResourceId.isJsonNull()) {
				resourceId = Long.valueOf(jsonResourceId.getAsLong());
			}
		}
	}

	public ThumbnailAction resourceId (Long resourceId) {
		this.resourceId = resourceId;
		return this;
	}
}

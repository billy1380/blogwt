// 
//  IosMessage.java
//  blogwt
// 
//  Created by William Shakour on June 16, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.helper.push;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class IosMessage extends MobileMessage {
	public String badge;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonBadge = badge == null ? JsonNull.INSTANCE
				: new JsonPrimitive(badge);
		object.add("badge", jsonBadge);
		return object;
	}

	public IosMessage badge (String badge) {
		this.badge = badge;
		return this;
	}
}
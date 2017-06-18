// 
//  WebMessage.java
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

public class WebMessage extends Message {
	public String icon;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonIcon = icon == null ? JsonNull.INSTANCE
				: new JsonPrimitive(icon);
		object.add("icon", jsonIcon);
		return object;
	}

	public WebMessage icon (String icon) {
		this.icon = icon;
		return this;
	}
}
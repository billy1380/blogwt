// 
//  AndroidMessage.java
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

public class AndroidMessage extends MobileMessage {
	public String android_channel_id;
	public String icon;
	public String tag;
	public String color;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonAndroid_channel_id = android_channel_id == null
				? JsonNull.INSTANCE : new JsonPrimitive(android_channel_id);
		object.add("android_channel_id", jsonAndroid_channel_id);
		JsonElement jsonIcon = icon == null ? JsonNull.INSTANCE
				: new JsonPrimitive(icon);
		object.add("icon", jsonIcon);
		JsonElement jsonTag = tag == null ? JsonNull.INSTANCE
				: new JsonPrimitive(tag);
		object.add("tag", jsonTag);
		JsonElement jsonColor = color == null ? JsonNull.INSTANCE
				: new JsonPrimitive(color);
		object.add("color", jsonColor);
		return object;
	}

	public AndroidMessage android_channel_id (String android_channel_id) {
		this.android_channel_id = android_channel_id;
		return this;
	}

	public AndroidMessage icon (String icon) {
		this.icon = icon;
		return this;
	}

	public AndroidMessage tag (String tag) {
		this.tag = tag;
		return this;
	}

	public AndroidMessage color (String color) {
		this.color = color;
		return this;
	}
}
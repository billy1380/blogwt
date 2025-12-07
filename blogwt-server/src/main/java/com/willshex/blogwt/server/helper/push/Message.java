// 
//  Message.java
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
import com.willshex.gson.shared.Jsonable;

public class Message extends Jsonable {
	public String title;
	public String body;
	public String click_action;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonTitle = title == null ? JsonNull.INSTANCE
				: new JsonPrimitive(title);
		object.add("title", jsonTitle);
		JsonElement jsonBody = body == null ? JsonNull.INSTANCE
				: new JsonPrimitive(body);
		object.add("body", jsonBody);
		JsonElement jsonClick_action = click_action == null ? JsonNull.INSTANCE
				: new JsonPrimitive(click_action);
		object.add("click_action", jsonClick_action);
		return object;
	}

	public Message title (String title) {
		this.title = title;
		return this;
	}

	public Message body (String body) {
		this.body = body;
		return this;
	}

	public Message click_action (String click_action) {
		this.click_action = click_action;
		return this;
	}
}
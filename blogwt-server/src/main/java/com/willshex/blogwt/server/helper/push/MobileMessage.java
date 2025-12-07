// 
//  MobileMessage.java
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

public class MobileMessage extends Message {
	public String sound;
	public String body_loc_key;
	public String body_loc_args;
	public String title_loc_key;
	public String title_loc_args;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonSound = sound == null ? JsonNull.INSTANCE
				: new JsonPrimitive(sound);
		object.add("sound", jsonSound);
		JsonElement jsonBody_loc_key = body_loc_key == null ? JsonNull.INSTANCE
				: new JsonPrimitive(body_loc_key);
		object.add("body_loc_key", jsonBody_loc_key);
		JsonElement jsonBody_loc_args = body_loc_args == null
				? JsonNull.INSTANCE : new JsonPrimitive(body_loc_args);
		object.add("body_loc_args", jsonBody_loc_args);
		JsonElement jsonTitle_loc_key = title_loc_key == null
				? JsonNull.INSTANCE : new JsonPrimitive(title_loc_key);
		object.add("title_loc_key", jsonTitle_loc_key);
		JsonElement jsonTitle_loc_args = title_loc_args == null
				? JsonNull.INSTANCE : new JsonPrimitive(title_loc_args);
		object.add("title_loc_args", jsonTitle_loc_args);
		return object;
	}

	public MobileMessage sound (String sound) {
		this.sound = sound;
		return this;
	}

	public MobileMessage body_loc_key (String body_loc_key) {
		this.body_loc_key = body_loc_key;
		return this;
	}

	public MobileMessage body_loc_args (String body_loc_args) {
		this.body_loc_args = body_loc_args;
		return this;
	}

	public MobileMessage title_loc_key (String title_loc_key) {
		this.title_loc_key = title_loc_key;
		return this;
	}

	public MobileMessage title_loc_args (String title_loc_args) {
		this.title_loc_args = title_loc_args;
		return this;
	}
}
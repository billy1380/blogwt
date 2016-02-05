//  
//  GetEmailAvatarResponse.java
//  blogwt
//
//  Created by William Shakour on August 31, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Response;

public class GetEmailAvatarResponse extends Response {
	public String avatar;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonAvatar = avatar == null ? JsonNull.INSTANCE
				: new JsonPrimitive(avatar);
		object.add("avatar", jsonAvatar);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("avatar")) {
			JsonElement jsonAvatar = jsonObject.get("avatar");
			if (jsonAvatar != null) {
				avatar = jsonAvatar.getAsString();
			}
		}
	}

	public GetEmailAvatarResponse avatar (String avatar) {
		this.avatar = avatar;
		return this;
	}
}
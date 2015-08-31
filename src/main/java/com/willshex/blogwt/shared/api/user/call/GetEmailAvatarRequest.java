//  
//  GetEmailAvatarRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 31, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;

public class GetEmailAvatarRequest extends Request {
	public String emailAddress;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonEmailAddress = emailAddress == null ? JsonNull.INSTANCE
				: new JsonPrimitive(emailAddress);
		object.add("emailAddress", jsonEmailAddress);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("emailAddress")) {
			JsonElement jsonEmailAddress = jsonObject.get("emailAddress");
			if (jsonEmailAddress != null) {
				emailAddress = jsonEmailAddress.getAsString();
			}
		}
	}

	public GetEmailAvatarRequest emailAddress (String emailAddress) {
		this.emailAddress = emailAddress;
		return this;
	}
}
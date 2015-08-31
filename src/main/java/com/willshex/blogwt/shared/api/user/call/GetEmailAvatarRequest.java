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

	public String email;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonEmail = email == null ? JsonNull.INSTANCE
				: new JsonPrimitive(email);
		object.add("email", jsonEmail);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("email")) {
			JsonElement jsonEmail = jsonObject.get("email");
			if (jsonEmail != null) {
				email = jsonEmail.getAsString();
			}
		}
	}

	public GetEmailAvatarRequest email (String email) {
		this.email = email;
		return this;
	}
}
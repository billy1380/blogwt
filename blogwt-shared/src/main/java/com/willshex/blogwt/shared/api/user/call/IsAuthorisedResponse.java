//  
//  IsAuthorisedResponse.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Response;

public class IsAuthorisedResponse extends Response {
	public Boolean authorised;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonAuthorised = authorised == null ? JsonNull.INSTANCE
				: new JsonPrimitive(authorised);
		object.add("authorised", jsonAuthorised);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("authorised")) {
			JsonElement jsonAuthorised = jsonObject.get("authorised");
			if (jsonAuthorised != null) {
				authorised = Boolean.valueOf(jsonAuthorised.getAsBoolean());
			}
		}
	}

	public IsAuthorisedResponse authorised (Boolean authorised) {
		this.authorised = authorised;
		return this;
	}
}
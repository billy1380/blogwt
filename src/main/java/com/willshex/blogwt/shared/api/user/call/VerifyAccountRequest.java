//  
//  VerifyAccountRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on September 6, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;

public class VerifyAccountRequest extends Request {
	public String actionCode;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonActionCode = actionCode == null ? JsonNull.INSTANCE
				: new JsonPrimitive(actionCode);
		object.add("actionCode", jsonActionCode);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("actionCode")) {
			JsonElement jsonActionCode = jsonObject.get("actionCode");
			if (jsonActionCode != null) {
				actionCode = jsonActionCode.getAsString();
			}
		}
	}

	public VerifyAccountRequest actionCode (String actionCode) {
		this.actionCode = actionCode;
		return this;
	}
}
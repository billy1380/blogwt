//  
//  ChangePasswordResponse.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;

public class ChangePasswordResponse extends Response {
	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
	}
}
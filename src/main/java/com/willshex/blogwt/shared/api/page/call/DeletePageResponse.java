//  
//  DeletePageResponse.java
//  blogwt
//
//  Created by William Shakour on June 30, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;

public class DeletePageResponse extends Response {
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
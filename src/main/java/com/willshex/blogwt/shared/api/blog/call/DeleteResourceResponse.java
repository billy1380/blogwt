//  
//  DeleteResourceResponse.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 10, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;

public class DeleteResourceResponse extends Response {
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
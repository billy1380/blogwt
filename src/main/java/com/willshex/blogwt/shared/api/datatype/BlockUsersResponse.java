//  
//  BlockUsersResponse.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;

public class BlockUsersResponse extends Response {
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
//  
//  GetArchiveEntriesRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.gson.web.service.shared.Request;

public class GetArchiveEntriesRequest extends Request {
	public Boolean includePosts;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonIncludePosts = includePosts == null ? JsonNull.INSTANCE
				: new JsonPrimitive(includePosts);
		object.add("includePosts", jsonIncludePosts);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("includePosts")) {
			JsonElement jsonIncludePosts = jsonObject.get("includePosts");
			if (jsonIncludePosts != null) {
				includePosts = Boolean.valueOf(jsonIncludePosts.getAsBoolean());
			}
		}
	}

	public GetArchiveEntriesRequest includePosts (Boolean includePosts) {
		this.includePosts = includePosts;
		return this;
	}
}
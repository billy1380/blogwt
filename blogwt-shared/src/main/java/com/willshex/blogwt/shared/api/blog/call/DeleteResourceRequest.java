//  
//  DeleteResourceRequest.java
//  blogwt
//
//  Created by William Shakour on August 10, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Resource;

public class DeleteResourceRequest extends Request {
	public Resource resource;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonResource = resource == null ? JsonNull.INSTANCE
				: resource.toJson();
		object.add("resource", jsonResource);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("resource")) {
			JsonElement jsonResource = jsonObject.get("resource");
			if (jsonResource != null) {
				resource = new Resource();
				resource.fromJson(jsonResource.getAsJsonObject());
			}
		}
	}

	public DeleteResourceRequest resource (Resource resource) {
		this.resource = resource;
		return this;
	}
}
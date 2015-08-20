//  
//  GetTagsResponse.java
//  blogwt
//
//  Created by William Shakour on July 15, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Tag;

public class GetTagsResponse extends Response {
	public Tag tags;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonTags = tags == null ? JsonNull.INSTANCE : tags.toJson();
		object.add("tags", jsonTags);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("tags")) {
			JsonElement jsonTags = jsonObject.get("tags");
			if (jsonTags != null) {
				tags = new Tag();
				tags.fromJson(jsonTags.getAsJsonObject());
			}
		}
	}

	public GetTagsResponse tags (Tag tags) {
		this.tags = tags;
		return this;
	}
}
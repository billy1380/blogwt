//  
//  GetPostRequest.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Post;

public class GetPostRequest extends Request {
	public Post post;
	public Boolean processTags;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPost = post == null ? JsonNull.INSTANCE : post.toJson();
		object.add("post", jsonPost);
		JsonElement jsonProcessTags = processTags == null ? JsonNull.INSTANCE
				: new JsonPrimitive(processTags);
		object.add("processTags", jsonProcessTags);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("post")) {
			JsonElement jsonPost = jsonObject.get("post");
			if (jsonPost != null) {
				post = new Post();
				post.fromJson(jsonPost.getAsJsonObject());
			}
		}
		if (jsonObject.has("processTags")) {
			JsonElement jsonProcessTags = jsonObject.get("processTags");
			if (jsonProcessTags != null && !jsonProcessTags.isJsonNull()) {
				processTags = Boolean.valueOf(jsonProcessTags.getAsBoolean());
			}
		}
	}

	public GetPostRequest post (Post post) {
		this.post = post;
		return this;
	}

	public GetPostRequest processTags (Boolean processTags) {
		this.processTags = processTags;
		return this;
	}
}
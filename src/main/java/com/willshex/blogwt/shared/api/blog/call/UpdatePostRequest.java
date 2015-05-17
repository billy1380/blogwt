//  
//  UpdatePostRequest.java
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

public class UpdatePostRequest extends Request {
	public Post post;
	public Boolean publish;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPost = post == null ? JsonNull.INSTANCE : post.toJson();
		object.add("post", jsonPost);
		JsonElement jsonPublish = publish == null ? JsonNull.INSTANCE
				: new JsonPrimitive(publish);
		object.add("publish", jsonPublish);
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
		if (jsonObject.has("publish")) {
			JsonElement jsonPublish = jsonObject.get("publish");
			if (jsonPublish != null) {
				publish = Boolean.valueOf(jsonPublish.getAsBoolean());
			}
		}
	}

	public UpdatePostRequest post (Post post) {
		this.post = post;
		return this;
	}

	public UpdatePostRequest publish (Boolean publish) {
		this.publish = publish;
		return this;
	}
}
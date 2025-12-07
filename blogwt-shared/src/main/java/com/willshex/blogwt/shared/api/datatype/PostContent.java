//  
//  PostContent.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;

@Entity
@Cache
public class PostContent extends DataType {
	public String body;

	public Key<Post> postKey;
	@Ignore public Post post;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonBody = body == null ? JsonNull.INSTANCE
				: new JsonPrimitive(body);
		object.add("body", jsonBody);
		JsonElement jsonPost = post == null ? JsonNull.INSTANCE : post.toJson();
		object.add("post", jsonPost);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("body")) {
			JsonElement jsonBody = jsonObject.get("body");
			if (jsonBody != null) {
				body = jsonBody.getAsString();
			}
		}
		if (jsonObject.has("post")) {
			JsonElement jsonPost = jsonObject.get("post");
			if (jsonPost != null) {
				post = new Post();
				post.fromJson(jsonPost.getAsJsonObject());
			}
		}
	}

	public PostContent body (String body) {
		this.body = body;
		return this;
	}

	public PostContent post (Post post) {
		this.post = post;
		return this;
	}
}
//  
//  UpdatePostResponse.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Post;

public class UpdatePostResponse extends Response {
	public Post post;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPost = post == null ? JsonNull.INSTANCE : post.toJson();
		object.add("post", jsonPost);
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
	}

	public UpdatePostResponse post (Post post) {
		this.post = post;
		return this;
	}
}
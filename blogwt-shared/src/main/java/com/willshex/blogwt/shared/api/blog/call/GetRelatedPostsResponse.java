//  
//  GetRelatedPostsResponse.java
//  blogwt
//
//  Created by William Shakour on August 7, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Post;

public class GetRelatedPostsResponse extends Response {
	public Post posts;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPosts = posts == null ? JsonNull.INSTANCE : posts
				.toJson();
		object.add("posts", jsonPosts);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("posts")) {
			JsonElement jsonPosts = jsonObject.get("posts");
			if (jsonPosts != null) {
				posts = new Post();
				posts.fromJson(jsonPosts.getAsJsonObject());
			}
		}
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetRelatedPostsResponse posts (Post posts) {
		this.posts = posts;
		return this;
	}

	public GetRelatedPostsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
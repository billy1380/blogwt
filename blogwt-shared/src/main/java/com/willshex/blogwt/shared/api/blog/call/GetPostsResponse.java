//  
//  GetPostsResponse.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Post;

public class GetPostsResponse extends Response {
	public List<Post> posts;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPosts = JsonNull.INSTANCE;
		if (posts != null) {
			jsonPosts = new JsonArray();
			for (int i = 0; i < posts.size(); i++) {
				JsonElement jsonPostsItem = posts.get(i) == null ? JsonNull.INSTANCE
						: posts.get(i).toJson();
				((JsonArray) jsonPosts).add(jsonPostsItem);
			}
		}
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
				posts = new ArrayList<Post>();
				Post item = null;
				for (int i = 0; i < jsonPosts.getAsJsonArray().size(); i++) {
					if (jsonPosts.getAsJsonArray().get(i) != null) {
						(item = new Post()).fromJson(jsonPosts.getAsJsonArray()
								.get(i).getAsJsonObject());
						posts.add(item);
					}
				}
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

	public GetPostsResponse posts (List<Post> posts) {
		this.posts = posts;
		return this;
	}

	public GetPostsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
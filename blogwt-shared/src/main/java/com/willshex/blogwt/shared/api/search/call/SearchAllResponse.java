//  
//  SearchAllResponse.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.search.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.User;

public class SearchAllResponse extends Response {
	public List<Post> posts;
	public List<Page> pages;
	public List<User> users;

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
		JsonElement jsonPages = JsonNull.INSTANCE;
		if (pages != null) {
			jsonPages = new JsonArray();
			for (int i = 0; i < pages.size(); i++) {
				JsonElement jsonPagesItem = pages.get(i) == null ? JsonNull.INSTANCE
						: pages.get(i).toJson();
				((JsonArray) jsonPages).add(jsonPagesItem);
			}
		}
		object.add("pages", jsonPages);
		JsonElement jsonUsers = JsonNull.INSTANCE;
		if (users != null) {
			jsonUsers = new JsonArray();
			for (int i = 0; i < users.size(); i++) {
				JsonElement jsonUsersItem = users.get(i) == null ? JsonNull.INSTANCE
						: users.get(i).toJson();
				((JsonArray) jsonUsers).add(jsonUsersItem);
			}
		}
		object.add("users", jsonUsers);
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

		if (jsonObject.has("pages")) {
			JsonElement jsonPages = jsonObject.get("pages");
			if (jsonPages != null) {
				pages = new ArrayList<Page>();
				Page item = null;
				for (int i = 0; i < jsonPages.getAsJsonArray().size(); i++) {
					if (jsonPages.getAsJsonArray().get(i) != null) {
						(item = new Page()).fromJson(jsonPages.getAsJsonArray()
								.get(i).getAsJsonObject());
						pages.add(item);
					}
				}
			}
		}

		if (jsonObject.has("users")) {
			JsonElement jsonUsers = jsonObject.get("users");
			if (jsonUsers != null) {
				users = new ArrayList<User>();
				User item = null;
				for (int i = 0; i < jsonUsers.getAsJsonArray().size(); i++) {
					if (jsonUsers.getAsJsonArray().get(i) != null) {
						(item = new User()).fromJson(jsonUsers.getAsJsonArray()
								.get(i).getAsJsonObject());
						users.add(item);
					}
				}
			}
		}

	}

	public SearchAllResponse posts (List<Post> posts) {
		this.posts = posts;
		return this;
	}

	public SearchAllResponse pages (List<Page> pages) {
		this.pages = pages;
		return this;
	}

	public SearchAllResponse users (List<User> users) {
		this.users = users;
		return this;
	}
}
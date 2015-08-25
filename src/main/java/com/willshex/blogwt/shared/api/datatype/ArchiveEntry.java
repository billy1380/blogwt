//  
//  ArchiveEntry.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class ArchiveEntry extends DataType {
	
	@Index public Integer year;
	@Index public Integer month;
	
	
	public List<Key<Post>> postKeys;
	@Ignore public List<Post> posts;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonYear = year == null ? JsonNull.INSTANCE
				: new JsonPrimitive(year);
		object.add("year", jsonYear);
		JsonElement jsonMonth = month == null ? JsonNull.INSTANCE
				: new JsonPrimitive(month);
		object.add("month", jsonMonth);
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
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("year")) {
			JsonElement jsonYear = jsonObject.get("year");
			if (jsonYear != null) {
				year = Integer.valueOf(jsonYear.getAsInt());
			}
		}
		if (jsonObject.has("month")) {
			JsonElement jsonMonth = jsonObject.get("month");
			if (jsonMonth != null) {
				month = Integer.valueOf(jsonMonth.getAsInt());
			}
		}
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

	}

	public ArchiveEntry year (Integer year) {
		this.year = year;
		return this;
	}

	public ArchiveEntry month (Integer month) {
		this.month = month;
		return this;
	}

	public ArchiveEntry posts (List<Post> posts) {
		this.posts = posts;
		return this;
	}
}
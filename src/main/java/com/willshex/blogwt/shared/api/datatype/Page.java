//  
//  Page.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 21, 2015.
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

public class Page extends DataType {
	public List<Post> posts;
	public String slug;
	public String shortTitle;
	public Page parent;
	public Boolean hasChildren;
	public Integer priority;

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
		JsonElement jsonSlug = slug == null ? JsonNull.INSTANCE
				: new JsonPrimitive(slug);
		object.add("slug", jsonSlug);
		JsonElement jsonShortTitle = shortTitle == null ? JsonNull.INSTANCE
				: new JsonPrimitive(shortTitle);
		object.add("shortTitle", jsonShortTitle);
		JsonElement jsonParent = parent == null ? JsonNull.INSTANCE : parent
				.toJson();
		object.add("parent", jsonParent);
		JsonElement jsonHasChildren = hasChildren == null ? JsonNull.INSTANCE
				: new JsonPrimitive(hasChildren);
		object.add("hasChildren", jsonHasChildren);
		JsonElement jsonPriority = priority == null ? JsonNull.INSTANCE
				: new JsonPrimitive(priority);
		object.add("priority", jsonPriority);
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

		if (jsonObject.has("slug")) {
			JsonElement jsonSlug = jsonObject.get("slug");
			if (jsonSlug != null) {
				slug = jsonSlug.getAsString();
			}
		}
		if (jsonObject.has("shortTitle")) {
			JsonElement jsonShortTitle = jsonObject.get("shortTitle");
			if (jsonShortTitle != null) {
				shortTitle = jsonShortTitle.getAsString();
			}
		}
		if (jsonObject.has("parent")) {
			JsonElement jsonParent = jsonObject.get("parent");
			if (jsonParent != null) {
				parent = new Page();
				parent.fromJson(jsonParent.getAsJsonObject());
			}
		}
		if (jsonObject.has("hasChildren")) {
			JsonElement jsonHasChildren = jsonObject.get("hasChildren");
			if (jsonHasChildren != null) {
				hasChildren = Boolean.valueOf(jsonHasChildren.getAsBoolean());
			}
		}
		if (jsonObject.has("priority")) {
			JsonElement jsonPriority = jsonObject.get("priority");
			if (jsonPriority != null) {
				priority = Integer.valueOf(jsonPriority.getAsInt());
			}
		}
	}

	public Page posts (List<Post> posts) {
		this.posts = posts;
		return this;
	}

	public Page slug (String slug) {
		this.slug = slug;
		return this;
	}

	public Page shortTitle (String shortTitle) {
		this.shortTitle = shortTitle;
		return this;
	}

	public Page parent (Page parent) {
		this.parent = parent;
		return this;
	}

	public Page hasChildren (Boolean hasChildren) {
		this.hasChildren = hasChildren;
		return this;
	}

	public Page priority (Integer priority) {
		this.priority = priority;
		return this;
	}
}
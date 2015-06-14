//  
//  Post.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.Date;
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
import com.googlecode.objectify.condition.IfNotNull;
import com.googlecode.objectify.condition.IfTrue;

@Entity
@Cache
public class Post extends DataType {
	@Index public Key<User> authorKey;
	@Ignore public User author;

	public List<String> tags;

	@Index(value = IfNotNull.class) public Date published;

	public String title;

	@Index public String slug;

	public String summary;

	public Key<PostContent> contentKey;
	@Ignore public PostContent content;

	@Index(value = IfTrue.class) public Boolean listed;
	public Boolean commentsEnabled;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonAuthor = author == null ? JsonNull.INSTANCE : author
				.toJson();
		object.add("author", jsonAuthor);
		JsonElement jsonTags = JsonNull.INSTANCE;
		if (tags != null) {
			jsonTags = new JsonArray();
			for (int i = 0; i < tags.size(); i++) {
				JsonElement jsonTagsItem = tags.get(i) == null ? JsonNull.INSTANCE
						: new JsonPrimitive(tags.get(i));
				((JsonArray) jsonTags).add(jsonTagsItem);
			}
		}
		object.add("tags", jsonTags);
		JsonElement jsonPublished = published == null ? JsonNull.INSTANCE
				: new JsonPrimitive(published.getTime());
		object.add("published", jsonPublished);
		JsonElement jsonTitle = title == null ? JsonNull.INSTANCE
				: new JsonPrimitive(title);
		object.add("title", jsonTitle);
		JsonElement jsonSlug = slug == null ? JsonNull.INSTANCE
				: new JsonPrimitive(slug);
		object.add("slug", jsonSlug);
		JsonElement jsonSummary = summary == null ? JsonNull.INSTANCE
				: new JsonPrimitive(summary);
		object.add("summary", jsonSummary);
		JsonElement jsonContent = content == null ? JsonNull.INSTANCE : content
				.toJson();
		object.add("content", jsonContent);
		JsonElement jsonListed = listed == null ? JsonNull.INSTANCE
				: new JsonPrimitive(listed);
		object.add("listed", jsonListed);
		JsonElement jsonCommentsEnabled = commentsEnabled == null ? JsonNull.INSTANCE
				: new JsonPrimitive(commentsEnabled);
		object.add("commentsEnabled", jsonCommentsEnabled);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("author")) {
			JsonElement jsonAuthor = jsonObject.get("author");
			if (jsonAuthor != null) {
				author = new User();
				author.fromJson(jsonAuthor.getAsJsonObject());
			}
		}
		if (jsonObject.has("tags")) {
			JsonElement jsonTags = jsonObject.get("tags");
			if (jsonTags != null) {
				tags = new ArrayList<String>();
				String item = null;
				for (int i = 0; i < jsonTags.getAsJsonArray().size(); i++) {
					if (jsonTags.getAsJsonArray().get(i) != null) {
						item = jsonTags.getAsJsonArray().get(i).getAsString();
						tags.add(item);
					}
				}
			}
		}

		if (jsonObject.has("published")) {
			JsonElement jsonPublished = jsonObject.get("published");
			if (jsonPublished != null) {
				published = new Date(jsonPublished.getAsLong());
			}
		}
		if (jsonObject.has("title")) {
			JsonElement jsonTitle = jsonObject.get("title");
			if (jsonTitle != null) {
				title = jsonTitle.getAsString();
			}
		}
		if (jsonObject.has("slug")) {
			JsonElement jsonSlug = jsonObject.get("slug");
			if (jsonSlug != null) {
				slug = jsonSlug.getAsString();
			}
		}
		if (jsonObject.has("summary")) {
			JsonElement jsonSummary = jsonObject.get("summary");
			if (jsonSummary != null) {
				summary = jsonSummary.getAsString();
			}
		}
		if (jsonObject.has("content")) {
			JsonElement jsonContent = jsonObject.get("content");
			if (jsonContent != null) {
				content = new PostContent();
				content.fromJson(jsonContent.getAsJsonObject());
			}
		}
		if (jsonObject.has("listed")) {
			JsonElement jsonListed = jsonObject.get("listed");
			if (jsonListed != null) {
				listed = Boolean.valueOf(jsonListed.getAsBoolean());
			}
		}
		if (jsonObject.has("commentsEnabled")) {
			JsonElement jsonCommentsEnabled = jsonObject.get("commentsEnabled");
			if (jsonCommentsEnabled != null) {
				commentsEnabled = Boolean.valueOf(jsonCommentsEnabled
						.getAsBoolean());
			}
		}
	}

	public Post author (User author) {
		this.author = author;
		return this;
	}

	public Post tags (List<String> tags) {
		this.tags = tags;
		return this;
	}

	public Post published (Date published) {
		this.published = published;
		return this;
	}

	public Post title (String title) {
		this.title = title;
		return this;
	}

	public Post slug (String slug) {
		this.slug = slug;
		return this;
	}

	public Post summary (String summary) {
		this.summary = summary;
		return this;
	}

	public Post content (PostContent content) {
		this.content = content;
		return this;
	}

	public Post listed (Boolean listed) {
		this.listed = listed;
		return this;
	}

	public Post commentsEnabled (Boolean commentsEnabled) {
		this.commentsEnabled = commentsEnabled;
		return this;
	}
}
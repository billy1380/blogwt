package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;

public class GetReactionsRequest extends Request {
	public Post post;
	public Page page;
	public Comment comment;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPost = post == null ? JsonNull.INSTANCE
				: post.toJson();
		object.add("post", jsonPost);
		JsonElement jsonPage = page == null ? JsonNull.INSTANCE
				: page.toJson();
		object.add("page", jsonPage);
		JsonElement jsonComment = comment == null ? JsonNull.INSTANCE
				: comment.toJson();
		object.add("comment", jsonComment);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("post")) {
			JsonElement jsonPost = jsonObject.get("post");
			if (jsonPost != null && !jsonPost.isJsonNull()) {
				post = new Post();
				post.fromJson(jsonPost.getAsJsonObject());
			}
		}
		if (jsonObject.has("page")) {
			JsonElement jsonPage = jsonObject.get("page");
			if (jsonPage != null && !jsonPage.isJsonNull()) {
				page = new Page();
				page.fromJson(jsonPage.getAsJsonObject());
			}
		}
		if (jsonObject.has("comment")) {
			JsonElement jsonComment = jsonObject.get("comment");
			if (jsonComment != null && !jsonComment.isJsonNull()) {
				comment = new Comment();
				comment.fromJson(jsonComment.getAsJsonObject());
			}
		}
	}

	public GetReactionsRequest post (Post post) {
		this.post = post;
		return this;
	}

	public GetReactionsRequest page (Page page) {
		this.page = page;
		return this;
	}

	public GetReactionsRequest comment (Comment comment) {
		this.comment = comment;
		return this;
	}
}

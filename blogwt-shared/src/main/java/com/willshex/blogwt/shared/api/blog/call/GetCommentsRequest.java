package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Post;

public class GetCommentsRequest extends Request {
	public Post post;
	public Page page;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPost = post == null ? JsonNull.INSTANCE
				: post.toJson();
		object.add("post", jsonPost);
		JsonElement jsonPage = page == null ? JsonNull.INSTANCE
				: page.toJson();
		object.add("page", jsonPage);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
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
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null && !jsonPager.isJsonNull()) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetCommentsRequest post (Post post) {
		this.post = post;
		return this;
	}

	public GetCommentsRequest page (Page page) {
		this.page = page;
		return this;
	}

	public GetCommentsRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}

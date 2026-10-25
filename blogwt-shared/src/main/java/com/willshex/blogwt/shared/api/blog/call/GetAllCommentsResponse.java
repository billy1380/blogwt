package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Comment;

public class GetAllCommentsResponse extends Response {
	public List<Comment> comments;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonComments = JsonNull.INSTANCE;
		if (comments != null) {
			jsonComments = new JsonArray();
			for (int i = 0; i < comments.size(); i++) {
				JsonElement jsonCommentsItem = comments.get(i) == null
						? JsonNull.INSTANCE : comments.get(i).toJson();
				((JsonArray) jsonComments).add(jsonCommentsItem);
			}
		}
		object.add("comments", jsonComments);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("comments")) {
			JsonElement jsonComments = jsonObject.get("comments");
			if (jsonComments != null && !jsonComments.isJsonNull()) {
				comments = new ArrayList<Comment>();
				Comment item = null;
				for (int i = 0; i < jsonComments.getAsJsonArray().size(); i++) {
					if (jsonComments.getAsJsonArray().get(i) != null) {
						(item = new Comment()).fromJson(jsonComments
								.getAsJsonArray().get(i).getAsJsonObject());
						comments.add(item);
					}
				}
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

	public GetAllCommentsResponse comments (List<Comment> comments) {
		this.comments = comments;
		return this;
	}

	public GetAllCommentsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}

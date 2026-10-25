package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Comment;

public class ApproveCommentResponse extends Response {
	public Comment comment;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonComment = comment == null ? JsonNull.INSTANCE
				: comment.toJson();
		object.add("comment", jsonComment);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("comment")) {
			JsonElement jsonComment = jsonObject.get("comment");
			if (jsonComment != null && !jsonComment.isJsonNull()) {
				comment = new Comment();
				comment.fromJson(jsonComment.getAsJsonObject());
			}
		}
	}

	public ApproveCommentResponse comment (Comment comment) {
		this.comment = comment;
		return this;
	}
}

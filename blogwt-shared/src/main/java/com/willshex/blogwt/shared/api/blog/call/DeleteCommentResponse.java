package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;

public class DeleteCommentResponse extends Response {
	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
	}
}

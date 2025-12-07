//  
//  GetPageRequest.java
//  blogwt
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Page;

public class GetPageRequest extends Request {
	public Page page;
	public Boolean includePosts;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPage = page == null ? JsonNull.INSTANCE : page.toJson();
		object.add("page", jsonPage);
		JsonElement jsonIncludePosts = includePosts == null ? JsonNull.INSTANCE
				: new JsonPrimitive(includePosts);
		object.add("includePosts", jsonIncludePosts);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("page")) {
			JsonElement jsonPage = jsonObject.get("page");
			if (jsonPage != null) {
				page = new Page();
				page.fromJson(jsonPage.getAsJsonObject());
			}
		}
		if (jsonObject.has("includePosts")) {
			JsonElement jsonIncludePosts = jsonObject.get("includePosts");
			if (jsonIncludePosts != null) {
				includePosts = Boolean.valueOf(jsonIncludePosts.getAsBoolean());
			}
		}
	}

	public GetPageRequest page (Page page) {
		this.page = page;
		return this;
	}

	public GetPageRequest includePosts (Boolean includePosts) {
		this.includePosts = includePosts;
		return this;
	}
}
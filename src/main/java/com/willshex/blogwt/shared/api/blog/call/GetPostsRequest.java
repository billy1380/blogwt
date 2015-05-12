//  
//  GetPostsRequest.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;

public class GetPostsRequest extends Request {
	public Pager pager;
	public Boolean summaryOnly;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonSummaryOnly = summaryOnly == null ? JsonNull.INSTANCE
				: new JsonPrimitive(summaryOnly);
		object.add("summaryOnly", jsonSummaryOnly);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
		if (jsonObject.has("summaryOnly")) {
			JsonElement jsonSummaryOnly = jsonObject.get("summaryOnly");
			if (jsonSummaryOnly != null) {
				summaryOnly = Boolean.valueOf(jsonSummaryOnly.getAsBoolean());
			}
		}
	}

	public GetPostsRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}

	public GetPostsRequest summaryOnly (Boolean summaryOnly) {
		this.summaryOnly = summaryOnly;
		return this;
	}
}
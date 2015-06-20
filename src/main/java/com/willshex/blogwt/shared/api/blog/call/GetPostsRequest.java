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
	public Boolean includePostContents;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonIncludePostContents = includePostContents == null ? JsonNull.INSTANCE
				: new JsonPrimitive(includePostContents);
		object.add("includePostContents", jsonIncludePostContents);
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
		if (jsonObject.has("includePostContents")) {
			JsonElement jsonIncludePostContents = jsonObject
					.get("includePostContents");
			if (jsonIncludePostContents != null) {
				includePostContents = Boolean.valueOf(jsonIncludePostContents
						.getAsBoolean());
			}
		}
	}

	public GetPostsRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}

	public GetPostsRequest includePostContents (Boolean includePostContents) {
		this.includePostContents = includePostContents;
		return this;
	}
}
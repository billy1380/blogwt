//  
//  GetPagesRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Page;

public class GetPagesRequest extends Request {
	public Page parent;
	public Boolean includePosts;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonParent = parent == null ? JsonNull.INSTANCE : parent
				.toJson();
		object.add("parent", jsonParent);
		JsonElement jsonIncludePosts = includePosts == null ? JsonNull.INSTANCE
				: new JsonPrimitive(includePosts);
		object.add("includePosts", jsonIncludePosts);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("parent")) {
			JsonElement jsonParent = jsonObject.get("parent");
			if (jsonParent != null) {
				parent = new Page();
				parent.fromJson(jsonParent.getAsJsonObject());
			}
		}
		if (jsonObject.has("includePosts")) {
			JsonElement jsonIncludePosts = jsonObject.get("includePosts");
			if (jsonIncludePosts != null) {
				includePosts = Boolean.valueOf(jsonIncludePosts.getAsBoolean());
			}
		}
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetPagesRequest parent (Page parent) {
		this.parent = parent;
		return this;
	}

	public GetPagesRequest includePosts (Boolean includePosts) {
		this.includePosts = includePosts;
		return this;
	}

	public GetPagesRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
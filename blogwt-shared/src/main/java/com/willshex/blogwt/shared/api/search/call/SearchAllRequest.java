//  
//  SearchAllRequest.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.search.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;

public class SearchAllRequest extends Request {
	public String query;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonQuery = query == null ? JsonNull.INSTANCE
				: new JsonPrimitive(query);
		object.add("query", jsonQuery);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("query")) {
			JsonElement jsonQuery = jsonObject.get("query");
			if (jsonQuery != null) {
				query = jsonQuery.getAsString();
			}
		}
	}

	public SearchAllRequest query (String query) {
		this.query = query;
		return this;
	}
}
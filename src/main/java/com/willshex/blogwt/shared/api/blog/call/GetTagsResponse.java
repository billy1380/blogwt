//  
//  GetTagsResponse.java
//  blogwt
//
//  Created by William Shakour on July 15, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Tag;

public class GetTagsResponse extends Response {
	public List<Tag> tags;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonTags = JsonNull.INSTANCE;
		if (tags != null) {
			jsonTags = new JsonArray();
			for (int i = 0; i < tags.size(); i++) {
				JsonElement jsonTagsItem = tags.get(i) == null
						? JsonNull.INSTANCE
						: tags.get(i).toJson();
				((JsonArray) jsonTags).add(jsonTagsItem);
			}
		}
		object.add("tags", jsonTags);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("tags")) {
			JsonElement jsonTags = jsonObject.get("tags");
			if (jsonTags != null) {
				tags = new ArrayList<Tag>();
				Tag item = null;
				for (int i = 0; i < jsonTags.getAsJsonArray().size(); i++) {
					if (jsonTags.getAsJsonArray().get(i) != null) {
						(item = new Tag()).fromJson(jsonTags.getAsJsonArray()
								.get(i).getAsJsonObject());
						tags.add(item);
					}
				}
			}
		}

	}

	/**
	
	  * Fluent setter for tags.
	  * 
	  * @param parameters
	  * @return
	  */

	public GetTagsResponse tags (List<Tag> tags) {
		this.tags = tags;
		return this;
	}
}
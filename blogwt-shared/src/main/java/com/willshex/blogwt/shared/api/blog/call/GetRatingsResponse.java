//  
//  GetRatingsResponse.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Rating;

public class GetRatingsResponse extends Response {
	public List<Rating> ratings;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonRatings = JsonNull.INSTANCE;
		if (ratings != null) {
			jsonRatings = new JsonArray();
			for (int i = 0; i < ratings.size(); i++) {
				JsonElement jsonRatingsItem = ratings.get(i) == null
						? JsonNull.INSTANCE : ratings.get(i).toJson();
				((JsonArray) jsonRatings).add(jsonRatingsItem);
			}
		}
		object.add("ratings", jsonRatings);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("ratings")) {
			JsonElement jsonRatings = jsonObject.get("ratings");
			if (jsonRatings != null) {
				ratings = new ArrayList<Rating>();
				Rating item = null;
				for (int i = 0; i < jsonRatings.getAsJsonArray().size(); i++) {
					if (jsonRatings.getAsJsonArray().get(i) != null) {
						(item = new Rating()).fromJson(jsonRatings
								.getAsJsonArray().get(i).getAsJsonObject());
						ratings.add(item);
					}
				}
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

	public GetRatingsResponse ratings (List<Rating> ratings) {
		this.ratings = ratings;
		return this;
	}

	public GetRatingsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
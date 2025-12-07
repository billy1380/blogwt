//  
//  SubmitRatingRequest.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Rating;

public class SubmitRatingRequest extends Request {
	public Rating rating;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonRating = rating == null ? JsonNull.INSTANCE
				: rating.toJson();
		object.add("rating", jsonRating);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("rating")) {
			JsonElement jsonRating = jsonObject.get("rating");
			if (jsonRating != null) {
				rating = new Rating();
				rating.fromJson(jsonRating.getAsJsonObject());
			}
		}
	}

	public SubmitRatingRequest rating (Rating rating) {
		this.rating = rating;
		return this;
	}
}
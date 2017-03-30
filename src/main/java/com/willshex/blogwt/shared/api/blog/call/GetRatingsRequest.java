//  
//  GetRatingsRequest.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.User;

public class GetRatingsRequest extends Request {

	public User by;
	public Long subjectId;
	public String subjectType;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonBy = by == null ? JsonNull.INSTANCE : by.toJson();
		object.add("by", jsonBy);
		JsonElement jsonSubjectId = subjectId == null ? JsonNull.INSTANCE
				: new JsonPrimitive(subjectId);
		object.add("subjectId", jsonSubjectId);
		JsonElement jsonSubjectType = subjectType == null ? JsonNull.INSTANCE
				: new JsonPrimitive(subjectType);
		object.add("subjectType", jsonSubjectType);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("by")) {
			JsonElement jsonBy = jsonObject.get("by");
			if (jsonBy != null) {
				by = new User();
				by.fromJson(jsonBy.getAsJsonObject());
			}
		}
		if (jsonObject.has("subjectId")) {
			JsonElement jsonSubjectId = jsonObject.get("subjectId");
			if (jsonSubjectId != null) {
				subjectId = Long.valueOf(jsonSubjectId.getAsLong());
			}
		}
		if (jsonObject.has("subjectType")) {
			JsonElement jsonSubjectType = jsonObject.get("subjectType");
			if (jsonSubjectType != null) {
				subjectType = jsonSubjectType.getAsString();
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

	public GetRatingsRequest by (User by) {
		this.by = by;
		return this;
	}

	public GetRatingsRequest subjectId (Long subjectId) {
		this.subjectId = subjectId;
		return this;
	}

	public GetRatingsRequest subjectType (String subjectType) {
		this.subjectType = subjectType;
		return this;
	}

	public GetRatingsRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
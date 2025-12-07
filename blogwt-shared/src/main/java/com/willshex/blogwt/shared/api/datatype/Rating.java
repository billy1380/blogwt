//  
//  Rating.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Rating extends DataType {
	@Index public Long subjectId;
	@Index public String subjectType;

	@Index public Key<User> byKey;
	@Ignore public User by;

	public Integer value;
	public String note;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonSubjectId = subjectId == null ? JsonNull.INSTANCE
				: new JsonPrimitive(subjectId);
		object.add("subjectId", jsonSubjectId);
		JsonElement jsonSubjectType = subjectType == null ? JsonNull.INSTANCE
				: new JsonPrimitive(subjectType);
		object.add("subjectType", jsonSubjectType);
		JsonElement jsonBy = by == null ? JsonNull.INSTANCE : by.toJson();
		object.add("by", jsonBy);
		JsonElement jsonValue = value == null ? JsonNull.INSTANCE
				: new JsonPrimitive(value);
		object.add("value", jsonValue);
		JsonElement jsonNote = note == null ? JsonNull.INSTANCE
				: new JsonPrimitive(note);
		object.add("note", jsonNote);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
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
		if (jsonObject.has("by")) {
			JsonElement jsonBy = jsonObject.get("by");
			if (jsonBy != null) {
				by = new User();
				by.fromJson(jsonBy.getAsJsonObject());
			}
		}
		if (jsonObject.has("value")) {
			JsonElement jsonValue = jsonObject.get("value");
			if (jsonValue != null) {
				value = Integer.valueOf(jsonValue.getAsInt());
			}
		}
		if (jsonObject.has("note")) {
			JsonElement jsonNote = jsonObject.get("note");
			if (jsonNote != null) {
				note = jsonNote.getAsString();
			}
		}
	}

	public Rating subjectId (Long subjectId) {
		this.subjectId = subjectId;
		return this;
	}

	public Rating subjectType (String subjectType) {
		this.subjectType = subjectType;
		return this;
	}

	public Rating by (User by) {
		this.by = by;
		return this;
	}

	public Rating value (Integer value) {
		this.value = value;
		return this;
	}

	public Rating note (String note) {
		this.note = note;
		return this;
	}
}
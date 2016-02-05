//  
//  Relationship.java
//  blogwt
//
//  Created by William Shakour on February 2, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
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
public class Relationship extends DataType {
	@Index public RelationshipTypeType type;

	@Index public Key<User> oneKey;
	@Ignore public User one;

	@Index public Key<User> anotherKey;
	@Ignore public User another;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonType = type == null ? JsonNull.INSTANCE
				: new JsonPrimitive(type.toString());
		object.add("type", jsonType);
		JsonElement jsonOne = one == null ? JsonNull.INSTANCE : one.toJson();
		object.add("one", jsonOne);
		JsonElement jsonAnother = another == null ? JsonNull.INSTANCE
				: another.toJson();
		object.add("another", jsonAnother);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("type")) {
			JsonElement jsonType = jsonObject.get("type");
			if (jsonType != null) {
				type = RelationshipTypeType.fromString(jsonType.getAsString());
			}
		}
		if (jsonObject.has("one")) {
			JsonElement jsonOne = jsonObject.get("one");
			if (jsonOne != null) {
				one = new User();
				one.fromJson(jsonOne.getAsJsonObject());
			}
		}
		if (jsonObject.has("another")) {
			JsonElement jsonAnother = jsonObject.get("another");
			if (jsonAnother != null) {
				another = new User();
				another.fromJson(jsonAnother.getAsJsonObject());
			}
		}
	}

	public Relationship type (RelationshipTypeType type) {
		this.type = type;
		return this;
	}

	public Relationship one (User one) {
		this.one = one;
		return this;
	}

	public Relationship another (User another) {
		this.another = another;
		return this;
	}
}
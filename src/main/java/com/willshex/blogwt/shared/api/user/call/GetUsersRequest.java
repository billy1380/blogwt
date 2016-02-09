//  
//  GetUsersRequest.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;

public class GetUsersRequest extends Request {
	public Pager pager;
	public String query;
	public User user;
	public RelationshipTypeType relationshipType;
	public Boolean userIsOther;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonQuery = query == null ? JsonNull.INSTANCE
				: new JsonPrimitive(query);
		object.add("query", jsonQuery);
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonRelationshipType = relationshipType == null
				? JsonNull.INSTANCE
				: new JsonPrimitive(relationshipType.toString());
		object.add("relationshipType", jsonRelationshipType);
		JsonElement jsonUserIsOther = userIsOther == null ? JsonNull.INSTANCE
				: new JsonPrimitive(userIsOther);
		object.add("userIsOther", jsonUserIsOther);
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
		if (jsonObject.has("query")) {
			JsonElement jsonQuery = jsonObject.get("query");
			if (jsonQuery != null) {
				query = jsonQuery.getAsString();
			}
		}
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
			}
		}
		if (jsonObject.has("relationshipType")) {
			JsonElement jsonRelationshipType = jsonObject
					.get("relationshipType");
			if (jsonRelationshipType != null) {
				relationshipType = RelationshipTypeType
						.fromString(jsonRelationshipType.getAsString());
			}
		}
		if (jsonObject.has("userIsOther")) {
			JsonElement jsonUserIsOther = jsonObject.get("userIsOther");
			if (jsonUserIsOther != null) {
				userIsOther = Boolean.valueOf(jsonUserIsOther.getAsBoolean());
			}
		}
	}

	public GetUsersRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}

	public GetUsersRequest query (String query) {
		this.query = query;
		return this;
	}

	public GetUsersRequest user (User user) {
		this.user = user;
		return this;
	}

	public GetUsersRequest relationshipType (
			RelationshipTypeType relationshipType) {
		this.relationshipType = relationshipType;
		return this;
	}

	public GetUsersRequest userIsOther (Boolean userIsOther) {
		this.userIsOther = userIsOther;
		return this;
	}
}
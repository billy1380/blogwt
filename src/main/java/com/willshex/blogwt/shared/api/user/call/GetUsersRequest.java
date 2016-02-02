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
	public User relatedTo;
	public RelationshipTypeType relationshipType;
	public Boolean reverse;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonQuery = query == null ? JsonNull.INSTANCE
				: new JsonPrimitive(query);
		object.add("query", jsonQuery);
		JsonElement jsonRelatedTo = relatedTo == null ? JsonNull.INSTANCE
				: relatedTo.toJson();
		object.add("relatedTo", jsonRelatedTo);
		JsonElement jsonRelationshipType = relationshipType == null
				? JsonNull.INSTANCE
				: new JsonPrimitive(relationshipType.toString());
		object.add("relationshipType", jsonRelationshipType);
		JsonElement jsonReverse = reverse == null ? JsonNull.INSTANCE
				: new JsonPrimitive(reverse);
		object.add("reverse", jsonReverse);
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
		if (jsonObject.has("relatedTo")) {
			JsonElement jsonRelatedTo = jsonObject.get("relatedTo");
			if (jsonRelatedTo != null) {
				relatedTo = new User();
				relatedTo.fromJson(jsonRelatedTo.getAsJsonObject());
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
		if (jsonObject.has("reverse")) {
			JsonElement jsonReverse = jsonObject.get("reverse");
			if (jsonReverse != null) {
				reverse = Boolean.valueOf(jsonReverse.getAsBoolean());
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

	public GetUsersRequest relatedTo (User relatedTo) {
		this.relatedTo = relatedTo;
		return this;
	}

	public GetUsersRequest relationshipType (
			RelationshipTypeType relationshipType) {
		this.relationshipType = relationshipType;
		return this;
	}

	public GetUsersRequest reverse (Boolean reverse) {
		this.reverse = reverse;
		return this;
	}
}
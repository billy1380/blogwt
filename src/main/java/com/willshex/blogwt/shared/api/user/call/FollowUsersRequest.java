//  
//  FollowUsersRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.User;

public class FollowUsersRequest extends Request {
	public User user;
	public List<User> others;
	public Boolean un;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonOthers = JsonNull.INSTANCE;
		if (others != null) {
			jsonOthers = new JsonArray();
			for (int i = 0; i < others.size(); i++) {
				JsonElement jsonOthersItem = others.get(i) == null
						? JsonNull.INSTANCE : others.get(i).toJson();
				((JsonArray) jsonOthers).add(jsonOthersItem);
			}
		}
		object.add("others", jsonOthers);
		JsonElement jsonUn = un == null ? JsonNull.INSTANCE
				: new JsonPrimitive(un);
		object.add("un", jsonUn);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
			}
		}
		if (jsonObject.has("others")) {
			JsonElement jsonOthers = jsonObject.get("others");
			if (jsonOthers != null) {
				others = new ArrayList<User>();
				User item = null;
				for (int i = 0; i < jsonOthers.getAsJsonArray().size(); i++) {
					if (jsonOthers.getAsJsonArray().get(i) != null) {
						(item = new User()).fromJson(jsonOthers.getAsJsonArray()
								.get(i).getAsJsonObject());
						others.add(item);
					}
				}
			}
		}

		if (jsonObject.has("un")) {
			JsonElement jsonUn = jsonObject.get("un");
			if (jsonUn != null) {
				un = Boolean.valueOf(jsonUn.getAsBoolean());
			}
		}
	}

	public FollowUsersRequest user (User user) {
		this.user = user;
		return this;
	}

	public FollowUsersRequest others (List<User> others) {
		this.others = others;
		return this;
	}

	public FollowUsersRequest un (Boolean un) {
		this.un = un;
		return this;
	}
}
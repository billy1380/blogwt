// 
//  Dealer.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype.bidly;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.willshex.blogwt.shared.api.datatype.DataType;

@Entity
@Cache
public class Dealer extends DataType {
	@Index public String name;

	@Ignore public List<Branch> branch;

	public Date suspended;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonBranch = JsonNull.INSTANCE;
		if (branch != null) {
			jsonBranch = new JsonArray();
			for (int i = 0; i < branch.size(); i++) {
				JsonElement jsonBranchItem = branch.get(i) == null
						? JsonNull.INSTANCE
						: branch.get(i).toJson();
				((JsonArray) jsonBranch).add(jsonBranchItem);
			}
		}
		object.add("branch", jsonBranch);
		JsonElement jsonSuspended = suspended == null ? JsonNull.INSTANCE
				: new JsonPrimitive(suspended.getTime());
		object.add("suspended", jsonSuspended);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
		if (jsonObject.has("branch")) {
			JsonElement jsonBranch = jsonObject.get("branch");
			if (jsonBranch != null) {
				branch = new ArrayList<Branch>();
				Branch item = null;
				for (int i = 0; i < jsonBranch.getAsJsonArray().size(); i++) {
					if (jsonBranch.getAsJsonArray().get(i) != null) {
						(item = new Branch()).fromJson(jsonBranch
								.getAsJsonArray().get(i).getAsJsonObject());
						branch.add(item);
					}
				}
			}
		}

		if (jsonObject.has("suspended")) {
			JsonElement jsonSuspended = jsonObject.get("suspended");
			if (jsonSuspended != null) {
				suspended = new Date(jsonSuspended.getAsLong());
			}
		}
	}

	public Dealer name (String name) {
		this.name = name;
		return this;
	}

	public Dealer branch (List<Branch> branch) {
		this.branch = branch;
		return this;
	}

	public Dealer suspended (Date suspended) {
		this.suspended = suspended;
		return this;
	}
}
// 
//  Branch.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.datatype.bidly;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.willshex.blogwt.shared.api.datatype.DataType;

@Entity
@Cache
public class Branch extends DataType {
	@Ignore public Dealer dealer;
	@Index public Key<Dealer> dealerKey;

	public Address location;
	public String name;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonDealer = dealer == null ? JsonNull.INSTANCE
				: dealer.toJson();
		object.add("dealer", jsonDealer);
		JsonElement jsonLocation = location == null ? JsonNull.INSTANCE
				: location.toJson();
		object.add("location", jsonLocation);
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("dealer")) {
			JsonElement jsonDealer = jsonObject.get("dealer");
			if (jsonDealer != null) {
				dealer = new Dealer();
				dealer.fromJson(jsonDealer.getAsJsonObject());
			}
		}
		if (jsonObject.has("location")) {
			JsonElement jsonLocation = jsonObject.get("location");
			if (jsonLocation != null) {
				location = new Address();
				location.fromJson(jsonLocation.getAsJsonObject());
			}
		}
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
	}

	public Branch dealer (Dealer dealer) {
		this.dealer = dealer;
		return this;
	}

	public Branch location (Address location) {
		this.location = location;
		return this;
	}

	public Branch name (String name) {
		this.name = name;
		return this;
	}
}
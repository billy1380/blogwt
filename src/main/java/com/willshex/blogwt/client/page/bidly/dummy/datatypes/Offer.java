// 
//  Offer.java
//  bidly
// 
//  Created by William Shakour on December 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.page.bidly.dummy.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.bidly.Address;

public class Offer extends DataType {
	public User user;
	public OfferStatusType status;
	public Build build;
	public Address address;
	public List<String> messages;
	public Date expires;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonStatus = status == null ? JsonNull.INSTANCE
				: new JsonPrimitive(status.toString());
		object.add("status", jsonStatus);
		JsonElement jsonBuild = build == null ? JsonNull.INSTANCE
				: build.toJson();
		object.add("build", jsonBuild);
		JsonElement jsonAddress = address == null ? JsonNull.INSTANCE
				: address.toJson();
		object.add("address", jsonAddress);
		JsonElement jsonMessages = JsonNull.INSTANCE;
		if (messages != null) {
			jsonMessages = new JsonArray();
			for (int i = 0; i < messages.size(); i++) {
				JsonElement jsonMessagesItem = messages.get(i) == null
						? JsonNull.INSTANCE
						: new JsonPrimitive(messages.get(i));
				((JsonArray) jsonMessages).add(jsonMessagesItem);
			}
		}
		object.add("messages", jsonMessages);
		JsonElement jsonExpires = expires == null ? JsonNull.INSTANCE
				: new JsonPrimitive(expires.getTime());
		object.add("expires", jsonExpires);
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
		if (jsonObject.has("status")) {
			JsonElement jsonStatus = jsonObject.get("status");
			if (jsonStatus != null) {
				status = OfferStatusType.fromString(jsonStatus.getAsString());
			}
		}
		if (jsonObject.has("build")) {
			JsonElement jsonBuild = jsonObject.get("build");
			if (jsonBuild != null) {
				build = new Build();
				build.fromJson(jsonBuild.getAsJsonObject());
			}
		}
		if (jsonObject.has("address")) {
			JsonElement jsonAddress = jsonObject.get("address");
			if (jsonAddress != null) {
				address = new Address();
				address.fromJson(jsonAddress.getAsJsonObject());
			}
		}
		if (jsonObject.has("messages")) {
			JsonElement jsonMessages = jsonObject.get("messages");
			if (jsonMessages != null) {
				messages = new ArrayList<String>();
				String item = null;
				for (int i = 0; i < jsonMessages.getAsJsonArray().size(); i++) {
					if (jsonMessages.getAsJsonArray().get(i) != null) {
						item = jsonMessages.getAsJsonArray().get(i)
								.getAsString();
						messages.add(item);
					}
				}
			}
		}

		if (jsonObject.has("expires")) {
			JsonElement jsonExpires = jsonObject.get("expires");
			if (jsonExpires != null) {
				expires = new Date(jsonExpires.getAsLong());
			}
		}
	}

	public Offer user (User user) {
		this.user = user;
		return this;
	}

	public Offer status (OfferStatusType status) {
		this.status = status;
		return this;
	}

	public Offer build (Build build) {
		this.build = build;
		return this;
	}

	public Offer address (Address address) {
		this.address = address;
		return this;
	}

	public Offer messages (List<String> messages) {
		this.messages = messages;
		return this;
	}

	public Offer expires (Date expires) {
		this.expires = expires;
		return this;
	}
}
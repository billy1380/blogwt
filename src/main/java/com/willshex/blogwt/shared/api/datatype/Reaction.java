// 
//  Reaction.java
//  blogwt
// 
//  Created by William Shakour on June 8, 2020.
//  Copyright © 2020 WillShex Limited. All rights reserved.
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

@Entity
@Cache
public class Reaction extends DataType {
	public String emoticon;

	public Key<DataType> parentKey;
	@Ignore public DataType parent;

	public Key<User> ownerKey;
	@Ignore public User owner;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonEmoticon = emoticon == null ? JsonNull.INSTANCE
				: new JsonPrimitive(emoticon);
		object.add("emoticon", jsonEmoticon);
		JsonElement jsonParent = parent == null ? JsonNull.INSTANCE
				: parent.toJson();
		object.add("parent", jsonParent);
		JsonElement jsonOwner = owner == null ? JsonNull.INSTANCE : owner.toJson();
		object.add("owner", jsonOwner);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("emoticon")) {
			JsonElement jsonEmoticon = jsonObject.get("emoticon");
			if (jsonEmoticon != null) {
				emoticon = jsonEmoticon.getAsString();
			}
		}
		if (jsonObject.has("parent")) {
			JsonElement jsonParent = jsonObject.get("parent");
			if (jsonParent != null) {
				parent = new DataType();
				parent.fromJson(jsonParent.getAsJsonObject());
			}
		}
		if (jsonObject.has("owner")) {
			JsonElement jsonOwner = jsonObject.get("owner");
			if (jsonOwner != null) {
				owner = new User();
				owner.fromJson(jsonOwner.getAsJsonObject());
			}
		}
	}

	/**
	* Fluent setter for emoticon.
	* 
	* @param emoticon
	* @return
	*/
	public Reaction emoticon (String emoticon) {
		this.emoticon = emoticon;
		return this;
	}

	/**
	* Fluent setter for parent.
	* 
	* @param parent
	* @return
	*/
	public Reaction parent (DataType parent) {
		this.parent = parent;
		return this;
	}

	/**
	* Fluent setter for owner.
	* 
	* @param owner
	* @return
	*/
	public Reaction owner (User owner) {
		this.owner = owner;
		return this;
	}
}
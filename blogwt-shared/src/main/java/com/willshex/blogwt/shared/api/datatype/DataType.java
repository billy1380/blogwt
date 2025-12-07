//  
//  DataType.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.willshex.gson.shared.Jsonable;

public class DataType extends Jsonable {
	@Id public Long id;
	@Index public Date created;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonId = id == null ? JsonNull.INSTANCE
				: new JsonPrimitive(id);
		object.add("id", jsonId);
		JsonElement jsonCreated = created == null ? JsonNull.INSTANCE
				: new JsonPrimitive(created.getTime());
		object.add("created", jsonCreated);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("id")) {
			JsonElement jsonId = jsonObject.get("id");
			if (jsonId != null) {
				id = Long.valueOf(jsonId.getAsLong());
			}
		}
		if (jsonObject.has("created")) {
			JsonElement jsonCreated = jsonObject.get("created");
			if (jsonCreated != null) {
				created = new Date(jsonCreated.getAsLong());
			}
		}
	}

	public DataType id (Long id) {
		this.id = id;
		return this;
	}

	public DataType created (Date created) {
		this.created = created;
		return this;
	}
}
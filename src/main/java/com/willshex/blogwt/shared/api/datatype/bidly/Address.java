// 
//  Address.java
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
import com.willshex.gson.shared.Jsonable;

public class Address extends Jsonable {
	public String nameOrNumber;
	public String postcode;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonNameOrNumber = nameOrNumber == null ? JsonNull.INSTANCE
				: new JsonPrimitive(nameOrNumber);
		object.add("nameOrNumber", jsonNameOrNumber);
		JsonElement jsonPostcode = postcode == null ? JsonNull.INSTANCE
				: new JsonPrimitive(postcode);
		object.add("postcode", jsonPostcode);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("nameOrNumber")) {
			JsonElement jsonNameOrNumber = jsonObject.get("nameOrNumber");
			if (jsonNameOrNumber != null) {
				nameOrNumber = jsonNameOrNumber.getAsString();
			}
		}
		if (jsonObject.has("postcode")) {
			JsonElement jsonPostcode = jsonObject.get("postcode");
			if (jsonPostcode != null) {
				postcode = jsonPostcode.getAsString();
			}
		}
	}

	public Address nameOrNumber (String nameOrNumber) {
		this.nameOrNumber = nameOrNumber;
		return this;
	}

	public Address postcode (String postcode) {
		this.postcode = postcode;
		return this;
	}
}
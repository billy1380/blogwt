//  
//  Form.java
//  blogwt
//
//  Created by William Shakour on November 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Form extends DataType {
	public String name;
	public List<Field> fields;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE
				: new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonFields = JsonNull.INSTANCE;
		if (fields != null) {
			jsonFields = new JsonArray();
			for (int i = 0; i < fields.size(); i++) {
				JsonElement jsonFieldsItem = fields.get(i) == null ? JsonNull.INSTANCE
						: fields.get(i).toJson();
				((JsonArray) jsonFields).add(jsonFieldsItem);
			}
		}
		object.add("fields", jsonFields);
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
		if (jsonObject.has("fields")) {
			JsonElement jsonFields = jsonObject.get("fields");
			if (jsonFields != null) {
				fields = new ArrayList<Field>();
				Field item = null;
				for (int i = 0; i < jsonFields.getAsJsonArray().size(); i++) {
					if (jsonFields.getAsJsonArray().get(i) != null) {
						(item = new Field()).fromJson(jsonFields
								.getAsJsonArray().get(i).getAsJsonObject());
						fields.add(item);
					}
				}
			}
		}

	}

	public Form name (String name) {
		this.name = name;
		return this;
	}

	public Form fields (List<Field> fields) {
		this.fields = fields;
		return this;
	}
}
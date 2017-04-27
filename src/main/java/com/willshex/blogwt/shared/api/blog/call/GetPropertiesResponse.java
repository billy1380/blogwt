// 
//  GetPropertiesResponse.java
//  blogwt
// 
//  Created by William Shakour on April 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Property;

public class GetPropertiesResponse extends Response {
	public List<Property> properties;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonProperties = JsonNull.INSTANCE;
		if (properties != null) {
			jsonProperties = new JsonArray();
			for (int i = 0; i < properties.size(); i++) {
				JsonElement jsonPropertiesItem = properties.get(i) == null
						? JsonNull.INSTANCE : properties.get(i).toJson();
				((JsonArray) jsonProperties).add(jsonPropertiesItem);
			}
		}
		object.add("properties", jsonProperties);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("properties")) {
			JsonElement jsonProperties = jsonObject.get("properties");
			if (jsonProperties != null) {
				properties = new ArrayList<Property>();
				Property item = null;
				for (int i = 0; i < jsonProperties.getAsJsonArray()
						.size(); i++) {
					if (jsonProperties.getAsJsonArray().get(i) != null) {
						(item = new Property()).fromJson(jsonProperties
								.getAsJsonArray().get(i).getAsJsonObject());
						properties.add(item);
					}
				}
			}
		}

		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetPropertiesResponse properties (List<Property> properties) {
		this.properties = properties;
		return this;
	}

	public GetPropertiesResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
//  
//  SetupBlogRequest.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;

public class SetupBlogRequest extends Request {
	public List<Property> properties;
	public List<User> users;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonProperties = JsonNull.INSTANCE;
		if (properties != null) {
			jsonProperties = new JsonArray();
			for (int i = 0; i < properties.size(); i++) {
				JsonElement jsonPropertiesItem = properties.get(i) == null ? JsonNull.INSTANCE
						: properties.get(i).toJson();
				((JsonArray) jsonProperties).add(jsonPropertiesItem);
			}
		}
		object.add("properties", jsonProperties);
		JsonElement jsonUsers = JsonNull.INSTANCE;
		if (users != null) {
			jsonUsers = new JsonArray();
			for (int i = 0; i < users.size(); i++) {
				JsonElement jsonUsersItem = users.get(i) == null ? JsonNull.INSTANCE
						: users.get(i).toJson();
				((JsonArray) jsonUsers).add(jsonUsersItem);
			}
		}
		object.add("users", jsonUsers);
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
				for (int i = 0; i < jsonProperties.getAsJsonArray().size(); i++) {
					if (jsonProperties.getAsJsonArray().get(i) != null) {
						(item = new Property()).fromJson(jsonProperties
								.getAsJsonArray().get(i).getAsJsonObject());
						properties.add(item);
					}
				}
			}
		}

		if (jsonObject.has("users")) {
			JsonElement jsonUsers = jsonObject.get("users");
			if (jsonUsers != null) {
				users = new ArrayList<User>();
				User item = null;
				for (int i = 0; i < jsonUsers.getAsJsonArray().size(); i++) {
					if (jsonUsers.getAsJsonArray().get(i) != null) {
						(item = new User()).fromJson(jsonUsers.getAsJsonArray()
								.get(i).getAsJsonObject());
						users.add(item);
					}
				}
			}
		}

	}

	public SetupBlogRequest properties (List<Property> properties) {
		this.properties = properties;
		return this;
	}

	public SetupBlogRequest users (List<User> users) {
		this.users = users;
		return this;
	}
}
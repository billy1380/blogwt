//  
//  GetPermissionsResponse.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Permission;

public class GetPermissionsResponse extends Response {
	public List<Permission> permissions;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPermissions = JsonNull.INSTANCE;
		if (permissions != null) {
			jsonPermissions = new JsonArray();
			for (int i = 0; i < permissions.size(); i++) {
				JsonElement jsonPermissionsItem = permissions.get(i) == null ? JsonNull.INSTANCE
						: permissions.get(i).toJson();
				((JsonArray) jsonPermissions).add(jsonPermissionsItem);
			}
		}
		object.add("permissions", jsonPermissions);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("permissions")) {
			JsonElement jsonPermissions = jsonObject.get("permissions");
			if (jsonPermissions != null) {
				permissions = new ArrayList<Permission>();
				Permission item = null;
				for (int i = 0; i < jsonPermissions.getAsJsonArray().size(); i++) {
					if (jsonPermissions.getAsJsonArray().get(i) != null) {
						(item = new Permission()).fromJson(jsonPermissions
								.getAsJsonArray().get(i).getAsJsonObject());
						permissions.add(item);
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

	public GetPermissionsResponse permissions (List<Permission> permissions) {
		this.permissions = permissions;
		return this;
	}

	public GetPermissionsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
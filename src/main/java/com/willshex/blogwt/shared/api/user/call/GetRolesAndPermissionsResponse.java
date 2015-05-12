//  
//  GetRolesAndPermissionsResponse.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;

public class GetRolesAndPermissionsResponse extends Response {
	public List<Role> roles;
	public List<Permission> permission;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonRoles = JsonNull.INSTANCE;
		if (roles != null) {
			jsonRoles = new JsonArray();
			for (int i = 0; i < roles.size(); i++) {
				JsonElement jsonRolesItem = roles.get(i) == null ? JsonNull.INSTANCE
						: roles.get(i).toJson();
				((JsonArray) jsonRoles).add(jsonRolesItem);
			}
		}
		object.add("roles", jsonRoles);
		JsonElement jsonPermission = JsonNull.INSTANCE;
		if (permission != null) {
			jsonPermission = new JsonArray();
			for (int i = 0; i < permission.size(); i++) {
				JsonElement jsonPermissionItem = permission.get(i) == null ? JsonNull.INSTANCE
						: permission.get(i).toJson();
				((JsonArray) jsonPermission).add(jsonPermissionItem);
			}
		}
		object.add("permission", jsonPermission);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("roles")) {
			JsonElement jsonRoles = jsonObject.get("roles");
			if (jsonRoles != null) {
				roles = new ArrayList<Role>();
				Role item = null;
				for (int i = 0; i < jsonRoles.getAsJsonArray().size(); i++) {
					if (jsonRoles.getAsJsonArray().get(i) != null) {
						(item = new Role()).fromJson(jsonRoles.getAsJsonArray()
								.get(i).getAsJsonObject());
						roles.add(item);
					}
				}
			}
		}

		if (jsonObject.has("permission")) {
			JsonElement jsonPermission = jsonObject.get("permission");
			if (jsonPermission != null) {
				permission = new ArrayList<Permission>();
				Permission item = null;
				for (int i = 0; i < jsonPermission.getAsJsonArray().size(); i++) {
					if (jsonPermission.getAsJsonArray().get(i) != null) {
						(item = new Permission()).fromJson(jsonPermission
								.getAsJsonArray().get(i).getAsJsonObject());
						permission.add(item);
					}
				}
			}
		}

	}

	public GetRolesAndPermissionsResponse roles (List<Role> roles) {
		this.roles = roles;
		return this;
	}

	public GetRolesAndPermissionsResponse permission (
			List<Permission> permission) {
		this.permission = permission;
		return this;
	}
}
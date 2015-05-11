//  
//  GetRolesAndPermissionsRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Request;

public class GetRolesAndPermissionsRequest extends Request {
	public Boolean idsOnly;
	public Boolean expandRoles;
	public Boolean rolesOnly;
	public Boolean permissionOnly;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonIdsOnly = idsOnly == null ? JsonNull.INSTANCE
				: new JsonPrimitive(idsOnly);
		object.add("idsOnly", jsonIdsOnly);
		JsonElement jsonExpandRoles = expandRoles == null ? JsonNull.INSTANCE
				: new JsonPrimitive(expandRoles);
		object.add("expandRoles", jsonExpandRoles);
		JsonElement jsonRolesOnly = rolesOnly == null ? JsonNull.INSTANCE
				: new JsonPrimitive(rolesOnly);
		object.add("rolesOnly", jsonRolesOnly);
		JsonElement jsonPermissionOnly = permissionOnly == null ? JsonNull.INSTANCE
				: new JsonPrimitive(permissionOnly);
		object.add("permissionOnly", jsonPermissionOnly);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("idsOnly")) {
			JsonElement jsonIdsOnly = jsonObject.get("idsOnly");
			if (jsonIdsOnly != null) {
				idsOnly = Boolean.valueOf(jsonIdsOnly.getAsBoolean());
			}
		}
		if (jsonObject.has("expandRoles")) {
			JsonElement jsonExpandRoles = jsonObject.get("expandRoles");
			if (jsonExpandRoles != null) {
				expandRoles = Boolean.valueOf(jsonExpandRoles.getAsBoolean());
			}
		}
		if (jsonObject.has("rolesOnly")) {
			JsonElement jsonRolesOnly = jsonObject.get("rolesOnly");
			if (jsonRolesOnly != null) {
				rolesOnly = Boolean.valueOf(jsonRolesOnly.getAsBoolean());
			}
		}
		if (jsonObject.has("permissionOnly")) {
			JsonElement jsonPermissionOnly = jsonObject.get("permissionOnly");
			if (jsonPermissionOnly != null) {
				permissionOnly = Boolean.valueOf(jsonPermissionOnly
						.getAsBoolean());
			}
		}
	}

	public GetRolesAndPermissionsRequest idsOnly (Boolean idsOnly) {
		this.idsOnly = idsOnly;
		return this;
	}

	public GetRolesAndPermissionsRequest expandRoles (Boolean expandRoles) {
		this.expandRoles = expandRoles;
		return this;
	}

	public GetRolesAndPermissionsRequest rolesOnly (Boolean rolesOnly) {
		this.rolesOnly = rolesOnly;
		return this;
	}

	public GetRolesAndPermissionsRequest permissionOnly (Boolean permissionOnly) {
		this.permissionOnly = permissionOnly;
		return this;
	}
}
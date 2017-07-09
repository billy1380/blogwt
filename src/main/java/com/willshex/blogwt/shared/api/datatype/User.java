//  
//  User.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class User extends DataType {
	@Index public String username;
	@Index public String email;

	public List<Key<Permission>> permissionKeys;
	@Ignore public List<Permission> permissions;

	public List<Key<Role>> roleKeys;
	@Ignore public List<Role> roles;

	@Index public String forename;
	@Index public String surname;
	public String avatar;
	public String group;
	public String password;
	public Date lastLoggedIn;
	public String summary;
	public Boolean verified;
	@Index public Date added;
	public Date expires;
	@Index public String actionCode;

	public Date suspendUntil;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonUsername = username == null ? JsonNull.INSTANCE
				: new JsonPrimitive(username);
		object.add("username", jsonUsername);
		JsonElement jsonEmail = email == null ? JsonNull.INSTANCE
				: new JsonPrimitive(email);
		object.add("email", jsonEmail);
		JsonElement jsonPermissions = JsonNull.INSTANCE;
		if (permissions != null) {
			jsonPermissions = new JsonArray();
			for (int i = 0; i < permissions.size(); i++) {
				JsonElement jsonPermissionsItem = permissions.get(i) == null
						? JsonNull.INSTANCE : permissions.get(i).toJson();
				((JsonArray) jsonPermissions).add(jsonPermissionsItem);
			}
		}
		object.add("permissions", jsonPermissions);
		JsonElement jsonRoles = JsonNull.INSTANCE;
		if (roles != null) {
			jsonRoles = new JsonArray();
			for (int i = 0; i < roles.size(); i++) {
				JsonElement jsonRolesItem = roles.get(i) == null
						? JsonNull.INSTANCE : roles.get(i).toJson();
				((JsonArray) jsonRoles).add(jsonRolesItem);
			}
		}
		object.add("roles", jsonRoles);
		JsonElement jsonForename = forename == null ? JsonNull.INSTANCE
				: new JsonPrimitive(forename);
		object.add("forename", jsonForename);
		JsonElement jsonSurname = surname == null ? JsonNull.INSTANCE
				: new JsonPrimitive(surname);
		object.add("surname", jsonSurname);
		JsonElement jsonAvatar = avatar == null ? JsonNull.INSTANCE
				: new JsonPrimitive(avatar);
		object.add("avatar", jsonAvatar);
		JsonElement jsonGroup = group == null ? JsonNull.INSTANCE
				: new JsonPrimitive(group);
		object.add("group", jsonGroup);
		JsonElement jsonPassword = password == null ? JsonNull.INSTANCE
				: new JsonPrimitive(password);
		object.add("password", jsonPassword);
		JsonElement jsonLastLoggedIn = lastLoggedIn == null ? JsonNull.INSTANCE
				: new JsonPrimitive(lastLoggedIn.getTime());
		object.add("lastLoggedIn", jsonLastLoggedIn);
		JsonElement jsonSummary = summary == null ? JsonNull.INSTANCE
				: new JsonPrimitive(summary);
		object.add("summary", jsonSummary);
		JsonElement jsonVerified = verified == null ? JsonNull.INSTANCE
				: new JsonPrimitive(verified);
		object.add("verified", jsonVerified);
		JsonElement jsonSuspendUntil = suspendUntil == null ? JsonNull.INSTANCE
				: new JsonPrimitive(suspendUntil.getTime());
		object.add("suspendUntil", jsonSuspendUntil);
		JsonElement jsonAdded = added == null ? JsonNull.INSTANCE
				: new JsonPrimitive(added.getTime());
		object.add("added", jsonAdded);
		JsonElement jsonExpires = expires == null ? JsonNull.INSTANCE
				: new JsonPrimitive(expires.getTime());
		object.add("expires", jsonExpires);
		JsonElement jsonActionCode = actionCode == null ? JsonNull.INSTANCE
				: new JsonPrimitive(actionCode);
		object.add("actionCode", jsonActionCode);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("username")) {
			JsonElement jsonUsername = jsonObject.get("username");
			if (jsonUsername != null) {
				username = jsonUsername.getAsString();
			}
		}
		if (jsonObject.has("email")) {
			JsonElement jsonEmail = jsonObject.get("email");
			if (jsonEmail != null) {
				email = jsonEmail.getAsString();
			}
		}
		if (jsonObject.has("permissions")) {
			JsonElement jsonPermissions = jsonObject.get("permissions");
			if (jsonPermissions != null) {
				permissions = new ArrayList<Permission>();
				Permission item = null;
				for (int i = 0; i < jsonPermissions.getAsJsonArray()
						.size(); i++) {
					if (jsonPermissions.getAsJsonArray().get(i) != null) {
						(item = new Permission()).fromJson(jsonPermissions
								.getAsJsonArray().get(i).getAsJsonObject());
						permissions.add(item);
					}
				}
			}
		}

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

		if (jsonObject.has("forename")) {
			JsonElement jsonForename = jsonObject.get("forename");
			if (jsonForename != null) {
				forename = jsonForename.getAsString();
			}
		}
		if (jsonObject.has("surname")) {
			JsonElement jsonSurname = jsonObject.get("surname");
			if (jsonSurname != null) {
				surname = jsonSurname.getAsString();
			}
		}
		if (jsonObject.has("avatar")) {
			JsonElement jsonAvatar = jsonObject.get("avatar");
			if (jsonAvatar != null) {
				avatar = jsonAvatar.getAsString();
			}
		}
		if (jsonObject.has("group")) {
			JsonElement jsonGroup = jsonObject.get("group");
			if (jsonGroup != null) {
				group = jsonGroup.getAsString();
			}
		}
		if (jsonObject.has("password")) {
			JsonElement jsonPassword = jsonObject.get("password");
			if (jsonPassword != null) {
				password = jsonPassword.getAsString();
			}
		}
		if (jsonObject.has("lastLoggedIn")) {
			JsonElement jsonLastLoggedIn = jsonObject.get("lastLoggedIn");
			if (jsonLastLoggedIn != null) {
				lastLoggedIn = new Date(jsonLastLoggedIn.getAsLong());
			}
		}
		if (jsonObject.has("summary")) {
			JsonElement jsonSummary = jsonObject.get("summary");
			if (jsonSummary != null) {
				summary = jsonSummary.getAsString();
			}
		}
		if (jsonObject.has("verified")) {
			JsonElement jsonVerified = jsonObject.get("verified");
			if (jsonVerified != null) {
				verified = Boolean.valueOf(jsonVerified.getAsBoolean());
			}
		}
		if (jsonObject.has("suspendUntil")) {
			JsonElement jsonSuspendUntil = jsonObject.get("suspendUntil");
			if (jsonSuspendUntil != null) {
				suspendUntil = new Date(jsonSuspendUntil.getAsLong());
			}
		}
		if (jsonObject.has("added")) {
			JsonElement jsonAdded = jsonObject.get("added");
			if (jsonAdded != null) {
				added = new Date(jsonAdded.getAsLong());
			}
		}
		if (jsonObject.has("expires")) {
			JsonElement jsonExpires = jsonObject.get("expires");
			if (jsonExpires != null) {
				expires = new Date(jsonExpires.getAsLong());
			}
		}
		if (jsonObject.has("actionCode")) {
			JsonElement jsonActionCode = jsonObject.get("actionCode");
			if (jsonActionCode != null) {
				actionCode = jsonActionCode.getAsString();
			}
		}
	}

	public User username (String username) {
		this.username = username;
		return this;
	}

	public User email (String email) {
		this.email = email;
		return this;
	}

	public User permissions (List<Permission> permissions) {
		this.permissions = permissions;
		return this;
	}

	public User roles (List<Role> roles) {
		this.roles = roles;
		return this;
	}

	public User forename (String forename) {
		this.forename = forename;
		return this;
	}

	public User surname (String surname) {
		this.surname = surname;
		return this;
	}

	public User avatar (String avatar) {
		this.avatar = avatar;
		return this;
	}

	public User group (String group) {
		this.group = group;
		return this;
	}

	public User password (String password) {
		this.password = password;
		return this;
	}

	public User lastLoggedIn (Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
		return this;
	}

	public User summary (String summary) {
		this.summary = summary;
		return this;
	}

	public User verified (Boolean verified) {
		this.verified = verified;
		return this;
	}

	public User suspendUntil (Date suspendUntil) {
		this.suspendUntil = suspendUntil;
		return this;
	}

	public User added (Date added) {
		this.added = added;
		return this;
	}

	public User expires (Date expires) {
		this.expires = expires;
		return this;
	}

	public User actionCode (String actionCode) {
		this.actionCode = actionCode;
		return this;
	}
}

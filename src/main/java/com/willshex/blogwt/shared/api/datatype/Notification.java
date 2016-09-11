//  
//  Notification.java
//  blogwt
//
//  Created by William Shakour on January 26, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;

@Entity
@Cache
public class Notification extends DataType {
	public Key<MetaNotification> metaKey;
	@Ignore public MetaNotification meta;

	public Key<User> userKey;
	@Ignore public User user;

	public List<Key<User>> userKeys;
	@Ignore public List<User> users;

	public List<Key<Page>> pageKeys;
	@Ignore public List<Page> pages;

	public List<Key<Post>> postKeys;
	@Ignore public List<Post> posts;

	public List<Key<Permission>> permissionKeys;
	@Ignore public List<Permission> permissions;

	public List<Key<Role>> roleKeys;
	@Ignore public List<Role> role;

	public List<Key<Resource>> resourceKeys;
	@Ignore public List<Resource> resources;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonMeta = meta == null ? JsonNull.INSTANCE : meta.toJson();
		object.add("meta", jsonMeta);
		JsonElement jsonUser = user == null ? JsonNull.INSTANCE : user.toJson();
		object.add("user", jsonUser);
		JsonElement jsonUsers = JsonNull.INSTANCE;
		if (users != null) {
			jsonUsers = new JsonArray();
			for (int i = 0; i < users.size(); i++) {
				JsonElement jsonUsersItem = users.get(i) == null
						? JsonNull.INSTANCE : users.get(i).toJson();
				((JsonArray) jsonUsers).add(jsonUsersItem);
			}
		}
		object.add("users", jsonUsers);
		JsonElement jsonPages = JsonNull.INSTANCE;
		if (pages != null) {
			jsonPages = new JsonArray();
			for (int i = 0; i < pages.size(); i++) {
				JsonElement jsonPagesItem = pages.get(i) == null
						? JsonNull.INSTANCE : pages.get(i).toJson();
				((JsonArray) jsonPages).add(jsonPagesItem);
			}
		}
		object.add("pages", jsonPages);
		JsonElement jsonPosts = JsonNull.INSTANCE;
		if (posts != null) {
			jsonPosts = new JsonArray();
			for (int i = 0; i < posts.size(); i++) {
				JsonElement jsonPostsItem = posts.get(i) == null
						? JsonNull.INSTANCE : posts.get(i).toJson();
				((JsonArray) jsonPosts).add(jsonPostsItem);
			}
		}
		object.add("posts", jsonPosts);
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
		JsonElement jsonRole = JsonNull.INSTANCE;
		if (role != null) {
			jsonRole = new JsonArray();
			for (int i = 0; i < role.size(); i++) {
				JsonElement jsonRoleItem = role.get(i) == null
						? JsonNull.INSTANCE : role.get(i).toJson();
				((JsonArray) jsonRole).add(jsonRoleItem);
			}
		}
		object.add("role", jsonRole);
		JsonElement jsonResources = JsonNull.INSTANCE;
		if (resources != null) {
			jsonResources = new JsonArray();
			for (int i = 0; i < resources.size(); i++) {
				JsonElement jsonResourcesItem = resources.get(i) == null
						? JsonNull.INSTANCE : resources.get(i).toJson();
				((JsonArray) jsonResources).add(jsonResourcesItem);
			}
		}
		object.add("resources", jsonResources);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("meta")) {
			JsonElement jsonMeta = jsonObject.get("meta");
			if (jsonMeta != null) {
				meta = new MetaNotification();
				meta.fromJson(jsonMeta.getAsJsonObject());
			}
		}
		if (jsonObject.has("user")) {
			JsonElement jsonUser = jsonObject.get("user");
			if (jsonUser != null) {
				user = new User();
				user.fromJson(jsonUser.getAsJsonObject());
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

		if (jsonObject.has("pages")) {
			JsonElement jsonPages = jsonObject.get("pages");
			if (jsonPages != null) {
				pages = new ArrayList<Page>();
				Page item = null;
				for (int i = 0; i < jsonPages.getAsJsonArray().size(); i++) {
					if (jsonPages.getAsJsonArray().get(i) != null) {
						(item = new Page()).fromJson(jsonPages.getAsJsonArray()
								.get(i).getAsJsonObject());
						pages.add(item);
					}
				}
			}
		}

		if (jsonObject.has("posts")) {
			JsonElement jsonPosts = jsonObject.get("posts");
			if (jsonPosts != null) {
				posts = new ArrayList<Post>();
				Post item = null;
				for (int i = 0; i < jsonPosts.getAsJsonArray().size(); i++) {
					if (jsonPosts.getAsJsonArray().get(i) != null) {
						(item = new Post()).fromJson(jsonPosts.getAsJsonArray()
								.get(i).getAsJsonObject());
						posts.add(item);
					}
				}
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

		if (jsonObject.has("role")) {
			JsonElement jsonRole = jsonObject.get("role");
			if (jsonRole != null) {
				role = new ArrayList<Role>();
				Role item = null;
				for (int i = 0; i < jsonRole.getAsJsonArray().size(); i++) {
					if (jsonRole.getAsJsonArray().get(i) != null) {
						(item = new Role()).fromJson(jsonRole.getAsJsonArray()
								.get(i).getAsJsonObject());
						role.add(item);
					}
				}
			}
		}

		if (jsonObject.has("resources")) {
			JsonElement jsonResources = jsonObject.get("resources");
			if (jsonResources != null) {
				resources = new ArrayList<Resource>();
				Resource item = null;
				for (int i = 0; i < jsonResources.getAsJsonArray()
						.size(); i++) {
					if (jsonResources.getAsJsonArray().get(i) != null) {
						(item = new Resource()).fromJson(jsonResources
								.getAsJsonArray().get(i).getAsJsonObject());
						resources.add(item);
					}
				}
			}
		}

	}

	public Notification meta (MetaNotification meta) {
		this.meta = meta;
		return this;
	}

	public Notification user (User user) {
		this.user = user;
		return this;
	}

	public Notification users (List<User> users) {
		this.users = users;
		return this;
	}

	public Notification pages (List<Page> pages) {
		this.pages = pages;
		return this;
	}

	public Notification posts (List<Post> posts) {
		this.posts = posts;
		return this;
	}

	public Notification permissions (List<Permission> permissions) {
		this.permissions = permissions;
		return this;
	}

	public Notification role (List<Role> role) {
		this.role = role;
		return this;
	}

	public Notification resources (List<Resource> resources) {
		this.resources = resources;
		return this;
	}
}
// 
//  Comment.java
//  blogwt
// 
//  Created by William Shakour on June 8, 2020.
//  Copyright © 2020 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.shared.api.datatype;

import java.util.ArrayList;
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

@Entity
@Cache
public class Comment extends DataType {
	public String body;

	public Key<User> ownerKey;
	@Ignore public User owner;

	public List<Key<Reaction>> reactionKeys;
	@Ignore public List<Reaction> reactions;

	public Key<DataType> parentKey;
	@Ignore public DataType parent;

	public List<Key<Comment>> replyKeys;
	@Ignore public List<Comment> replies;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonBody = body == null ? JsonNull.INSTANCE
				: new JsonPrimitive(body);
		object.add("body", jsonBody);
		JsonElement jsonOwner = owner == null ? JsonNull.INSTANCE : owner.toJson();
		object.add("owner", jsonOwner);
		JsonElement jsonReactions = JsonNull.INSTANCE;
		if (reactions != null) {
			jsonReactions = new JsonArray();
			for (int i = 0; i < reactions.size(); i++) {
				JsonElement jsonReactionsItem = reactions.get(i) == null
						? JsonNull.INSTANCE
						: reactions.get(i).toJson();
				((JsonArray) jsonReactions).add(jsonReactionsItem);
			}
		}
		object.add("reactions", jsonReactions);
		JsonElement jsonParent = parent == null ? JsonNull.INSTANCE
				: parent.toJson();
		object.add("parent", jsonParent);
		JsonElement jsonReplies = JsonNull.INSTANCE;
		if (replies != null) {
			jsonReplies = new JsonArray();
			for (int i = 0; i < replies.size(); i++) {
				JsonElement jsonRepliesItem = replies.get(i) == null ? JsonNull.INSTANCE
						: replies.get(i).toJson();
				((JsonArray) jsonReplies).add(jsonRepliesItem);
			}
		}
		object.add("replies", jsonReplies);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("body")) {
			JsonElement jsonBody = jsonObject.get("body");
			if (jsonBody != null) {
				body = jsonBody.getAsString();
			}
		}
		if (jsonObject.has("owner")) {
			JsonElement jsonOwner = jsonObject.get("owner");
			if (jsonOwner != null) {
				owner = new User();
				owner.fromJson(jsonOwner.getAsJsonObject());
			}
		}
		if (jsonObject.has("reactions")) {
			JsonElement jsonReactions = jsonObject.get("reactions");
			if (jsonReactions != null) {
				reactions = new ArrayList<Reaction>();
				Reaction item = null;
				for (int i = 0; i < jsonReactions.getAsJsonArray().size(); i++) {
					if (jsonReactions.getAsJsonArray().get(i) != null) {
						(item = new Reaction()).fromJson(
								jsonReactions.getAsJsonArray().get(i).getAsJsonObject());
						reactions.add(item);
					}
				}
			}
		}

		if (jsonObject.has("parent")) {
			JsonElement jsonParent = jsonObject.get("parent");
			if (jsonParent != null) {
				parent = new DataType();
				parent.fromJson(jsonParent.getAsJsonObject());
			}
		}
		if (jsonObject.has("replies")) {
			JsonElement jsonReplies = jsonObject.get("replies");
			if (jsonReplies != null) {
				replies = new ArrayList<Comment>();
				Comment item = null;
				for (int i = 0; i < jsonReplies.getAsJsonArray().size(); i++) {
					if (jsonReplies.getAsJsonArray().get(i) != null) {
						(item = new Comment()).fromJson(
								jsonReplies.getAsJsonArray().get(i).getAsJsonObject());
						replies.add(item);
					}
				}
			}
		}

	}

	/**
	* Fluent setter for body.
	* 
	* @param body
	* @return
	*/
	public Comment body (String body) {
		this.body = body;
		return this;
	}

	/**
	* Fluent setter for owner.
	* 
	* @param owner
	* @return
	*/
	public Comment owner (User owner) {
		this.owner = owner;
		return this;
	}

	/**
	* Fluent setter for reactions.
	* 
	* @param reactions
	* @return
	*/
	public Comment reactions (List<Reaction> reactions) {
		this.reactions = reactions;
		return this;
	}

	/**
	* Fluent setter for parent.
	* 
	* @param parent
	* @return
	*/
	public Comment parent (DataType parent) {
		this.parent = parent;
		return this;
	}

	/**
	* Fluent setter for replies.
	* 
	* @param replies
	* @return
	*/
	public Comment replies (List<Comment> replies) {
		this.replies = replies;
		return this;
	}
}
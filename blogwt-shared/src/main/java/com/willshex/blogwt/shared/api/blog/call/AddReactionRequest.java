package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.Reaction;

public class AddReactionRequest extends Request {
	public Reaction reaction;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonReaction = reaction == null ? JsonNull.INSTANCE
				: reaction.toJson();
		object.add("reaction", jsonReaction);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("reaction")) {
			JsonElement jsonReaction = jsonObject.get("reaction");
			if (jsonReaction != null && !jsonReaction.isJsonNull()) {
				reaction = new Reaction();
				reaction.fromJson(jsonReaction.getAsJsonObject());
			}
		}
	}

	public AddReactionRequest reaction (Reaction reaction) {
		this.reaction = reaction;
		return this;
	}
}

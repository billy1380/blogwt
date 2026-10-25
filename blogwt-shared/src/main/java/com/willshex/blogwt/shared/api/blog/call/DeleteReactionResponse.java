package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Reaction;

public class DeleteReactionResponse extends Response {
	public List<Reaction> reactions;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonReactions = JsonNull.INSTANCE;
		if (reactions != null) {
			jsonReactions = new JsonArray();
			for (int i = 0; i < reactions.size(); i++) {
				JsonElement jsonReactionsItem = reactions.get(i) == null
						? JsonNull.INSTANCE : reactions.get(i).toJson();
				((JsonArray) jsonReactions).add(jsonReactionsItem);
			}
		}
		object.add("reactions", jsonReactions);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("reactions")) {
			JsonElement jsonReactions = jsonObject.get("reactions");
			if (jsonReactions != null && !jsonReactions.isJsonNull()) {
				reactions = new ArrayList<Reaction>();
				Reaction item = null;
				for (int i = 0; i < jsonReactions.getAsJsonArray().size(); i++) {
					if (jsonReactions.getAsJsonArray().get(i) != null) {
						(item = new Reaction()).fromJson(jsonReactions
								.getAsJsonArray().get(i).getAsJsonObject());
						reactions.add(item);
					}
				}
			}
		}
	}

	public DeleteReactionResponse reactions (List<Reaction> reactions) {
		this.reactions = reactions;
		return this;
	}
}

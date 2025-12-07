// 
//  DeleteGeneratedDownloadsRequest.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.download.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;

public class DeleteGeneratedDownloadsRequest extends Request {
	public List<GeneratedDownload> downloads;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonDownloads = JsonNull.INSTANCE;
		if (downloads != null) {
			jsonDownloads = new JsonArray();
			for (int i = 0; i < downloads.size(); i++) {
				JsonElement jsonDownloadsItem = downloads.get(i) == null
						? JsonNull.INSTANCE
						: downloads.get(i).toJson();
				((JsonArray) jsonDownloads).add(jsonDownloadsItem);
			}
		}
		object.add("downloads", jsonDownloads);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("downloads")) {
			JsonElement jsonDownloads = jsonObject.get("downloads");
			if (jsonDownloads != null) {
				downloads = new ArrayList<GeneratedDownload>();
				GeneratedDownload item = null;
				for (int i = 0; i < jsonDownloads.getAsJsonArray()
						.size(); i++) {
					if (jsonDownloads.getAsJsonArray().get(i) != null) {
						(item = new GeneratedDownload()).fromJson(jsonDownloads
								.getAsJsonArray().get(i).getAsJsonObject());
						downloads.add(item);
					}
				}
			}
		}

	}

	public DeleteGeneratedDownloadsRequest downloads (
			List<GeneratedDownload> downloads) {
		this.downloads = downloads;
		return this;
	}
}
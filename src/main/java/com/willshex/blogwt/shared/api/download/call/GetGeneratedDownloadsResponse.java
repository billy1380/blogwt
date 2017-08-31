// 
//  GetGeneratedDownloadsResponse.java
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
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;

public class GetGeneratedDownloadsResponse extends Response {
	public List<GeneratedDownload> downloads;
	public Pager pager;

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
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
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

		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetGeneratedDownloadsResponse downloads (
			List<GeneratedDownload> downloads) {
		this.downloads = downloads;
		return this;
	}

	public GetGeneratedDownloadsResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
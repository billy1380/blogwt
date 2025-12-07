//  
//  GetArchiveEntriesResponse.java
//  blogwt
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;

public class GetArchiveEntriesResponse extends Response {
	public List<ArchiveEntry> archive;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonArchive = JsonNull.INSTANCE;
		if (archive != null) {
			jsonArchive = new JsonArray();
			for (int i = 0; i < archive.size(); i++) {
				JsonElement jsonArchiveItem = archive.get(i) == null
						? JsonNull.INSTANCE : archive.get(i).toJson();
				((JsonArray) jsonArchive).add(jsonArchiveItem);
			}
		}
		object.add("archive", jsonArchive);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("archive")) {
			JsonElement jsonArchive = jsonObject.get("archive");
			if (jsonArchive != null) {
				archive = new ArrayList<ArchiveEntry>();
				ArchiveEntry item = null;
				for (int i = 0; i < jsonArchive.getAsJsonArray().size(); i++) {
					if (jsonArchive.getAsJsonArray().get(i) != null) {
						(item = new ArchiveEntry()).fromJson(jsonArchive
								.getAsJsonArray().get(i).getAsJsonObject());
						archive.add(item);
					}
				}
			}
		}

	}

	public GetArchiveEntriesResponse archive (List<ArchiveEntry> archive) {
		this.archive = archive;
		return this;
	}
}
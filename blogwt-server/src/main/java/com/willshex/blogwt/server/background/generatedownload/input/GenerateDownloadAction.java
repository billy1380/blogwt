// 
//  GenerateDownloadAction.java
//  blogwt
// 
//  Created by William Shakour on June 28, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.background.generatedownload.input;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.gson.web.service.shared.Request;

public class GenerateDownloadAction extends Request {
	public GeneratedDownload download;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonDownload = download == null ? JsonNull.INSTANCE
				: download.toJson();
		object.add("download", jsonDownload);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("download")) {
			JsonElement jsonDownload = jsonObject.get("download");
			if (jsonDownload != null) {
				download = new GeneratedDownload();
				download.fromJson(jsonDownload.getAsJsonObject());
			}
		}
	}

	public GenerateDownloadAction download (GeneratedDownload download) {
		this.download = download;
		return this;
	}
}
// 
//  GenerateDownloadRequest.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.shared.api.download.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;

public class GenerateDownloadRequest extends Request {
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

	public GenerateDownloadRequest download (GeneratedDownload download) {
		this.download = download;
		return this;
	}
}
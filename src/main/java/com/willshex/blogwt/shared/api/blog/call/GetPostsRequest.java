//  
//  GetPostsRequest.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;

public class GetPostsRequest extends Request {

	public Pager pager;
	public Boolean includePostContents;
	public String tag;
	public ArchiveEntry archiveEntry;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonIncludePostContents = includePostContents == null ? JsonNull.INSTANCE
				: new JsonPrimitive(includePostContents);
		object.add("includePostContents", jsonIncludePostContents);
		JsonElement jsonTag = tag == null ? JsonNull.INSTANCE
				: new JsonPrimitive(tag);
		object.add("tag", jsonTag);
		JsonElement jsonArchiveEntry = archiveEntry == null ? JsonNull.INSTANCE
				: archiveEntry.toJson();
		object.add("archiveEntry", jsonArchiveEntry);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
		if (jsonObject.has("includePostContents")) {
			JsonElement jsonIncludePostContents = jsonObject
					.get("includePostContents");
			if (jsonIncludePostContents != null) {
				includePostContents = Boolean.valueOf(jsonIncludePostContents
						.getAsBoolean());
			}
		}
		if (jsonObject.has("tag")) {
			JsonElement jsonTag = jsonObject.get("tag");
			if (jsonTag != null) {
				tag = jsonTag.getAsString();
			}
		}
		if (jsonObject.has("archiveEntry")) {
			JsonElement jsonArchiveEntry = jsonObject.get("archiveEntry");
			if (jsonArchiveEntry != null) {
				archiveEntry = new ArchiveEntry();
				archiveEntry.fromJson(jsonArchiveEntry.getAsJsonObject());
			}
		}
	}

	public GetPostsRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}

	public GetPostsRequest includePostContents (Boolean includePostContents) {
		this.includePostContents = includePostContents;
		return this;
	}

	public GetPostsRequest tag (String tag) {
		this.tag = tag;
		return this;
	}

	public GetPostsRequest archiveEntry (ArchiveEntry archiveEntry) {
		this.archiveEntry = archiveEntry;
		return this;
	}
}
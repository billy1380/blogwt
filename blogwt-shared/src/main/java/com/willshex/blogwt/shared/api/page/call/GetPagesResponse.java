//  
//  GetPagesResponse.java
//  blogwt
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Page;

public class GetPagesResponse extends Response {
	public List<Page> pages;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPages = JsonNull.INSTANCE;
		if (pages != null) {
			jsonPages = new JsonArray();
			for (int i = 0; i < pages.size(); i++) {
				JsonElement jsonPagesItem = pages.get(i) == null ? JsonNull.INSTANCE
						: pages.get(i).toJson();
				((JsonArray) jsonPages).add(jsonPagesItem);
			}
		}
		object.add("pages", jsonPages);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
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

		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetPagesResponse pages (List<Page> pages) {
		this.pages = pages;
		return this;
	}

	public GetPagesResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
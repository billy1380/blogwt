//  
//  GetResourcesResponse.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 10, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Resource;

public class GetResourcesResponse extends Response {
	public List<Resource> resources;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonResources = JsonNull.INSTANCE;
		if (resources != null) {
			jsonResources = new JsonArray();
			for (int i = 0; i < resources.size(); i++) {
				JsonElement jsonResourcesItem = resources.get(i) == null ? JsonNull.INSTANCE
						: resources.get(i).toJson();
				((JsonArray) jsonResources).add(jsonResourcesItem);
			}
		}
		object.add("resources", jsonResources);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE : pager
				.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("resources")) {
			JsonElement jsonResources = jsonObject.get("resources");
			if (jsonResources != null) {
				resources = new ArrayList<Resource>();
				Resource item = null;
				for (int i = 0; i < jsonResources.getAsJsonArray().size(); i++) {
					if (jsonResources.getAsJsonArray().get(i) != null) {
						(item = new Resource()).fromJson(jsonResources
								.getAsJsonArray().get(i).getAsJsonObject());
						resources.add(item);
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

	public GetResourcesResponse resources (List<Resource> resources) {
		this.resources = resources;
		return this;
	}

	public GetResourcesResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
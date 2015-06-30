//  
//  CreatePageResponse.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 30, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.Page;

public class CreatePageResponse extends Response {
	public Page page;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPage = page == null ? JsonNull.INSTANCE : page.toJson();
		object.add("page", jsonPage);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("page")) {
			JsonElement jsonPage = jsonObject.get("page");
			if (jsonPage != null) {
				page = new Page();
				page.fromJson(jsonPage.getAsJsonObject());
			}
		}
	}

	public CreatePageResponse page (Page page) {
		this.page = page;
		return this;
	}
}
//  
//  GetUsersRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 28, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;

public class GetUsersRequest extends Request {
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
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
	}

	public GetUsersRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
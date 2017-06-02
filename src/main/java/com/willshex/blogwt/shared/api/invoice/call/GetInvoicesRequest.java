//  
//  GetInvoicesRequest.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Request;

public class GetInvoicesRequest extends Request {
	public Boolean outstanding;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonOutstanding = outstanding == null ? JsonNull.INSTANCE
				: new JsonPrimitive(outstanding);
		object.add("outstanding", jsonOutstanding);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("outstanding")) {
			JsonElement jsonOutstanding = jsonObject.get("outstanding");
			if (jsonOutstanding != null) {
				outstanding = Boolean.valueOf(jsonOutstanding.getAsBoolean());
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

	public GetInvoicesRequest outstanding (Boolean outstanding) {
		this.outstanding = outstanding;
		return this;
	}

	public GetInvoicesRequest pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
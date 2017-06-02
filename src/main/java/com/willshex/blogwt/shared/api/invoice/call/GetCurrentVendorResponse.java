//  
//  GetCurrentVendorResponse.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;

public class GetCurrentVendorResponse extends Response {
	public Vendor vendor;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonVendor = vendor == null ? JsonNull.INSTANCE
				: vendor.toJson();
		object.add("vendor", jsonVendor);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("vendor")) {
			JsonElement jsonVendor = jsonObject.get("vendor");
			if (jsonVendor != null) {
				vendor = new Vendor();
				vendor.fromJson(jsonVendor.getAsJsonObject());
			}
		}
	}

	public GetCurrentVendorResponse vendor (Vendor vendor) {
		this.vendor = vendor;
		return this;
	}
}
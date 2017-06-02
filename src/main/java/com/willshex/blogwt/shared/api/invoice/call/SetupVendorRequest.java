//  
//  SetupVendorRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 26, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;

public class SetupVendorRequest extends Request {
	public Vendor vendor;
	public User admin;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonVendor = vendor == null ? JsonNull.INSTANCE : vendor.toJson();
		object.add("vendor", jsonVendor);
		JsonElement jsonAdmin = admin == null ? JsonNull.INSTANCE : admin.toJson();
		object.add("admin", jsonAdmin);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("vendor")) {
			JsonElement jsonVendor = jsonObject.get("vendor");
			if (jsonVendor != null) {
				vendor = new Vendor();
				vendor.fromJson(jsonVendor.getAsJsonObject());
			}
		}
		if (jsonObject.has("admin")) {
			JsonElement jsonAdmin = jsonObject.get("admin");
			if (jsonAdmin != null) {
				admin = new User();
				admin.fromJson(jsonAdmin.getAsJsonObject());
			}
		}
	}

	public SetupVendorRequest vendor(Vendor vendor) {
		this.vendor = vendor;
		return this;
	}

	public SetupVendorRequest admin(User admin) {
		this.admin = admin;
		return this;
	}
}
//  
//  AddCustomerRequest.java
//  xsdwsdl2code
//
//  Created by William Shakour on January 5, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;

public class AddCustomerRequest extends Request {
	public Customer customer;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonCustomer = customer == null ? JsonNull.INSTANCE
				: customer.toJson();
		object.add("customer", jsonCustomer);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("customer")) {
			JsonElement jsonCustomer = jsonObject.get("customer");
			if (jsonCustomer != null) {
				customer = new Customer();
				customer.fromJson(jsonCustomer.getAsJsonObject());
			}
		}
	}

	public AddCustomerRequest customer (Customer customer) {
		this.customer = customer;
		return this;
	}
}
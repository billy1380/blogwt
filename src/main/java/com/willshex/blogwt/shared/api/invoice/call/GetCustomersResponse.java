//  
//  GetCustomersResponse.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.Response;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;

public class GetCustomersResponse extends Response {
	public List<Customer> customers;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonCustomers = JsonNull.INSTANCE;
		if (customers != null) {
			jsonCustomers = new JsonArray();
			for (int i = 0; i < customers.size(); i++) {
				JsonElement jsonCustomersItem = customers.get(i) == null
						? JsonNull.INSTANCE : customers.get(i).toJson();
				((JsonArray) jsonCustomers).add(jsonCustomersItem);
			}
		}
		object.add("customers", jsonCustomers);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("customers")) {
			JsonElement jsonCustomers = jsonObject.get("customers");
			if (jsonCustomers != null) {
				customers = new ArrayList<Customer>();
				Customer item = null;
				for (int i = 0; i < jsonCustomers.getAsJsonArray()
						.size(); i++) {
					if (jsonCustomers.getAsJsonArray().get(i) != null) {
						(item = new Customer()).fromJson(jsonCustomers
								.getAsJsonArray().get(i).getAsJsonObject());
						customers.add(item);
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

	public GetCustomersResponse customers (List<Customer> customers) {
		this.customers = customers;
		return this;
	}

	public GetCustomersResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
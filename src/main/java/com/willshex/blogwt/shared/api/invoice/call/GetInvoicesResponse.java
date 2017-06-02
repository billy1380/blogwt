//  
//  GetInvoicesResponse.java
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
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;

public class GetInvoicesResponse extends Response {
	public Pager pager;
	public List<Invoice> invoices;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		JsonElement jsonInvoices = JsonNull.INSTANCE;
		if (invoices != null) {
			jsonInvoices = new JsonArray();
			for (int i = 0; i < invoices.size(); i++) {
				JsonElement jsonInvoicesItem = invoices.get(i) == null
						? JsonNull.INSTANCE : invoices.get(i).toJson();
				((JsonArray) jsonInvoices).add(jsonInvoicesItem);
			}
		}
		object.add("invoices", jsonInvoices);
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
		if (jsonObject.has("invoices")) {
			JsonElement jsonInvoices = jsonObject.get("invoices");
			if (jsonInvoices != null) {
				invoices = new ArrayList<Invoice>();
				Invoice item = null;
				for (int i = 0; i < jsonInvoices.getAsJsonArray().size(); i++) {
					if (jsonInvoices.getAsJsonArray().get(i) != null) {
						(item = new Invoice()).fromJson(jsonInvoices
								.getAsJsonArray().get(i).getAsJsonObject());
						invoices.add(item);
					}
				}
			}
		}

	}

	public GetInvoicesResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}

	public GetInvoicesResponse invoices (List<Invoice> invoices) {
		this.invoices = invoices;
		return this;
	}
}
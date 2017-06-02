//  
//  CreateInvoiceRequest.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.invoice.call;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;

public class CreateInvoiceRequest extends Request {
	public Invoice invoice;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonInvoice = invoice == null ? JsonNull.INSTANCE
				: invoice.toJson();
		object.add("invoice", jsonInvoice);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("invoice")) {
			JsonElement jsonInvoice = jsonObject.get("invoice");
			if (jsonInvoice != null) {
				invoice = new Invoice();
				invoice.fromJson(jsonInvoice.getAsJsonObject());
			}
		}
	}

	public CreateInvoiceRequest invoice (Invoice invoice) {
		this.invoice = invoice;
		return this;
	}
}
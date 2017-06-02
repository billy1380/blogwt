//  
//  SetInvoiceStatusRequest.java
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
import com.willshex.blogwt.shared.api.Request;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceStatusType;

public class SetInvoiceStatusRequest extends Request {
	public Invoice invoice;
	public InvoiceStatusType status;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonInvoice = invoice == null ? JsonNull.INSTANCE : invoice.toJson();
		object.add("invoice", jsonInvoice);
		JsonElement jsonStatus = status == null ? JsonNull.INSTANCE : new JsonPrimitive(status.toString());
		object.add("status", jsonStatus);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("invoice")) {
			JsonElement jsonInvoice = jsonObject.get("invoice");
			if (jsonInvoice != null) {
				invoice = new Invoice();
				invoice.fromJson(jsonInvoice.getAsJsonObject());
			}
		}
		if (jsonObject.has("status")) {
			JsonElement jsonStatus = jsonObject.get("status");
			if (jsonStatus != null) {
				status = InvoiceStatusType.fromString(jsonStatus.getAsString());
			}
		}
	}

	public SetInvoiceStatusRequest invoice(Invoice invoice) {
		this.invoice = invoice;
		return this;
	}

	public SetInvoiceStatusRequest status(InvoiceStatusType status) {
		this.status = status;
		return this;
	}
}
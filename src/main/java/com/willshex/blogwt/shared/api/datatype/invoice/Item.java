//  
//  Item.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.willshex.blogwt.shared.api.datatype.DataType;

@Entity
@Cache
public class Item extends DataType {
	public String name;
	@Index public Date date;
	public String description;
	public Integer price;
	public Float quantity;
	public String unit;

	@Index public Key<Invoice> invoiceKey;
	@Ignore public Invoice invoice;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE : new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonInvoice = invoice == null ? JsonNull.INSTANCE : invoice.toJson();
		object.add("invoice", jsonInvoice);
		JsonElement jsonDate = date == null ? JsonNull.INSTANCE : new JsonPrimitive(date.getTime());
		object.add("date", jsonDate);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE : new JsonPrimitive(description);
		object.add("description", jsonDescription);
		JsonElement jsonPrice = price == null ? JsonNull.INSTANCE : new JsonPrimitive(price);
		object.add("price", jsonPrice);
		JsonElement jsonQuantity = quantity == null ? JsonNull.INSTANCE : new JsonPrimitive(quantity);
		object.add("quantity", jsonQuantity);
		JsonElement jsonUnit = unit == null ? JsonNull.INSTANCE : new JsonPrimitive(unit);
		object.add("unit", jsonUnit);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
		if (jsonObject.has("invoice")) {
			JsonElement jsonInvoice = jsonObject.get("invoice");
			if (jsonInvoice != null) {
				invoice = new Invoice();
				invoice.fromJson(jsonInvoice.getAsJsonObject());
			}
		}
		if (jsonObject.has("date")) {
			JsonElement jsonDate = jsonObject.get("date");
			if (jsonDate != null) {
				date = new Date(jsonDate.getAsLong());
			}
		}
		if (jsonObject.has("description")) {
			JsonElement jsonDescription = jsonObject.get("description");
			if (jsonDescription != null) {
				description = jsonDescription.getAsString();
			}
		}
		if (jsonObject.has("price")) {
			JsonElement jsonPrice = jsonObject.get("price");
			if (jsonPrice != null) {
				price = Integer.valueOf(jsonPrice.getAsInt());
			}
		}
		if (jsonObject.has("quantity")) {
			JsonElement jsonQuantity = jsonObject.get("quantity");
			if (jsonQuantity != null) {
				quantity = Float.valueOf(jsonQuantity.getAsFloat());
			}
		}
		if (jsonObject.has("unit")) {
			JsonElement jsonUnit = jsonObject.get("unit");
			if (jsonUnit != null) {
				unit = jsonUnit.getAsString();
			}
		}
	}

	public Item name(String name) {
		this.name = name;
		return this;
	}

	public Item invoice(Invoice invoice) {
		this.invoice = invoice;
		return this;
	}

	public Item date(Date date) {
		this.date = date;
		return this;
	}

	public Item description(String description) {
		this.description = description;
		return this;
	}

	public Item price(Integer price) {
		this.price = price;
		return this;
	}

	public Item quantity(Float quantity) {
		this.quantity = quantity;
		return this;
	}

	public Item unit(String unit) {
		this.unit = unit;
		return this;
	}
}
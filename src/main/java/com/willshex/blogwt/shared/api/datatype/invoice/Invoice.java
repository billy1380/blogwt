//  
//  Invoice.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonArray;
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
public class Invoice extends DataType {
	@Ignore public List<Item> items;

	@Index public Key<Vendor> vendorKey;
	@Ignore public Vendor vendor;

	@Index public Key<Customer> customerKey;
	@Ignore public Customer customer;

	public Key<Currency> currencyKey;
	@Ignore public Currency currency;

	@Index public String name;
	@Index public Date date;
	@Index public InvoiceStatusType status;
	public Float tax;
	public Integer deposit;
	public Integer itemCount;
	public Integer totalPrice;

	public Key<BankAccount> accountKey;
	@Ignore public BankAccount account;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonItems = JsonNull.INSTANCE;
		if (items != null) {
			jsonItems = new JsonArray();
			for (int i = 0; i < items.size(); i++) {
				JsonElement jsonItemsItem = items.get(i) == null ? JsonNull.INSTANCE : items.get(i).toJson();
				((JsonArray) jsonItems).add(jsonItemsItem);
			}
		}
		object.add("items", jsonItems);
		JsonElement jsonVendor = vendor == null ? JsonNull.INSTANCE : vendor.toJson();
		object.add("vendor", jsonVendor);
		JsonElement jsonCustomer = customer == null ? JsonNull.INSTANCE : customer.toJson();
		object.add("customer", jsonCustomer);
		JsonElement jsonCurrency = currency == null ? JsonNull.INSTANCE : currency.toJson();
		object.add("currency", jsonCurrency);
		JsonElement jsonName = name == null ? JsonNull.INSTANCE : new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonDate = date == null ? JsonNull.INSTANCE : new JsonPrimitive(date.getTime());
		object.add("date", jsonDate);
		JsonElement jsonStatus = status == null ? JsonNull.INSTANCE : new JsonPrimitive(status.toString());
		object.add("status", jsonStatus);
		JsonElement jsonTax = tax == null ? JsonNull.INSTANCE : new JsonPrimitive(tax);
		object.add("tax", jsonTax);
		JsonElement jsonDeposit = deposit == null ? JsonNull.INSTANCE : new JsonPrimitive(deposit);
		object.add("deposit", jsonDeposit);
		JsonElement jsonItemCount = itemCount == null ? JsonNull.INSTANCE : new JsonPrimitive(itemCount);
		object.add("itemCount", jsonItemCount);
		JsonElement jsonTotalPrice = totalPrice == null ? JsonNull.INSTANCE : new JsonPrimitive(totalPrice);
		object.add("totalPrice", jsonTotalPrice);
		JsonElement jsonAccount = account == null ? JsonNull.INSTANCE : account.toJson();
		object.add("account", jsonAccount);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("items")) {
			JsonElement jsonItems = jsonObject.get("items");
			if (jsonItems != null) {
				items = new ArrayList<Item>();
				Item item = null;
				for (int i = 0; i < jsonItems.getAsJsonArray().size(); i++) {
					if (jsonItems.getAsJsonArray().get(i) != null) {
						(item = new Item()).fromJson(jsonItems.getAsJsonArray().get(i).getAsJsonObject());
						items.add(item);
					}
				}
			}
		}

		if (jsonObject.has("vendor")) {
			JsonElement jsonVendor = jsonObject.get("vendor");
			if (jsonVendor != null) {
				vendor = new Vendor();
				vendor.fromJson(jsonVendor.getAsJsonObject());
			}
		}
		if (jsonObject.has("customer")) {
			JsonElement jsonCustomer = jsonObject.get("customer");
			if (jsonCustomer != null) {
				customer = new Customer();
				customer.fromJson(jsonCustomer.getAsJsonObject());
			}
		}
		if (jsonObject.has("currency")) {
			JsonElement jsonCurrency = jsonObject.get("currency");
			if (jsonCurrency != null) {
				currency = new Currency();
				currency.fromJson(jsonCurrency.getAsJsonObject());
			}
		}
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
		if (jsonObject.has("date")) {
			JsonElement jsonDate = jsonObject.get("date");
			if (jsonDate != null) {
				date = new Date(jsonDate.getAsLong());
			}
		}
		if (jsonObject.has("status")) {
			JsonElement jsonStatus = jsonObject.get("status");
			if (jsonStatus != null) {
				status = InvoiceStatusType.fromString(jsonStatus.getAsString());
			}
		}
		if (jsonObject.has("tax")) {
			JsonElement jsonTax = jsonObject.get("tax");
			if (jsonTax != null) {
				tax = Float.valueOf(jsonTax.getAsFloat());
			}
		}
		if (jsonObject.has("deposit")) {
			JsonElement jsonDeposit = jsonObject.get("deposit");
			if (jsonDeposit != null) {
				deposit = Integer.valueOf(jsonDeposit.getAsInt());
			}
		}
		if (jsonObject.has("itemCount")) {
			JsonElement jsonItemCount = jsonObject.get("itemCount");
			if (jsonItemCount != null) {
				itemCount = Integer.valueOf(jsonItemCount.getAsInt());
			}
		}
		if (jsonObject.has("totalPrice")) {
			JsonElement jsonTotalPrice = jsonObject.get("totalPrice");
			if (jsonTotalPrice != null) {
				totalPrice = Integer.valueOf(jsonTotalPrice.getAsInt());
			}
		}
		if (jsonObject.has("account")) {
			JsonElement jsonAccount = jsonObject.get("account");
			if (jsonAccount != null) {
				account = new BankAccount();
				account.fromJson(jsonAccount.getAsJsonObject());
			}
		}
	}

	public Invoice items(List<Item> items) {
		this.items = items;
		return this;
	}

	public Invoice vendor(Vendor vendor) {
		this.vendor = vendor;
		return this;
	}

	public Invoice customer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public Invoice currency(Currency currency) {
		this.currency = currency;
		return this;
	}

	public Invoice name(String name) {
		this.name = name;
		return this;
	}

	public Invoice date(Date date) {
		this.date = date;
		return this;
	}

	public Invoice status(InvoiceStatusType status) {
		this.status = status;
		return this;
	}

	public Invoice tax(Float tax) {
		this.tax = tax;
		return this;
	}

	public Invoice deposit(Integer deposit) {
		this.deposit = deposit;
		return this;
	}

	public Invoice itemCount(Integer itemCount) {
		this.itemCount = itemCount;
		return this;
	}

	public Invoice totalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
		return this;
	}

	public Invoice account(BankAccount account) {
		this.account = account;
		return this;
	}
}
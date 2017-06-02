//  
//  Vendor.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import java.util.ArrayList;
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
import com.willshex.blogwt.shared.api.datatype.Resource;

@Entity
@Cache
public class Vendor extends DataType {
	public Key<Address> addressKey;
	@Ignore public Address address;

	@Index public String name;

	public Key<Resource> logoKey;
	@Ignore public Resource logo;

	@Index public String code;
	public String description;
	public String phone;
	@Index public String email;

	@Ignore public List<BankAccount> accounts;

	public Integer invoiceCount;
	public Integer outstandingCount;
	public Integer customerCount;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonAddress = address == null ? JsonNull.INSTANCE : address.toJson();
		object.add("address", jsonAddress);
		JsonElement jsonName = name == null ? JsonNull.INSTANCE : new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonLogo = logo == null ? JsonNull.INSTANCE : logo.toJson();
		object.add("logo", jsonLogo);
		JsonElement jsonCode = code == null ? JsonNull.INSTANCE : new JsonPrimitive(code);
		object.add("code", jsonCode);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE : new JsonPrimitive(description);
		object.add("description", jsonDescription);
		JsonElement jsonPhone = phone == null ? JsonNull.INSTANCE : new JsonPrimitive(phone);
		object.add("phone", jsonPhone);
		JsonElement jsonEmail = email == null ? JsonNull.INSTANCE : new JsonPrimitive(email);
		object.add("email", jsonEmail);
		JsonElement jsonInvoiceCount = invoiceCount == null ? JsonNull.INSTANCE : new JsonPrimitive(invoiceCount);
		object.add("invoiceCount", jsonInvoiceCount);
		JsonElement jsonOutstandingCount = outstandingCount == null ? JsonNull.INSTANCE : new JsonPrimitive(outstandingCount);
		object.add("outstandingCount", jsonOutstandingCount);
		JsonElement jsonCustomerCount = customerCount == null ? JsonNull.INSTANCE : new JsonPrimitive(customerCount);
		object.add("customerCount", jsonCustomerCount);
		JsonElement jsonAccounts = JsonNull.INSTANCE;
		if (accounts != null) {
			jsonAccounts = new JsonArray();
			for (int i = 0; i < accounts.size(); i++) {
				JsonElement jsonAccountsItem = accounts.get(i) == null ? JsonNull.INSTANCE : accounts.get(i).toJson();
				((JsonArray) jsonAccounts).add(jsonAccountsItem);
			}
		}
		object.add("accounts", jsonAccounts);

		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("address")) {
			JsonElement jsonAddress = jsonObject.get("address");
			if (jsonAddress != null) {
				address = new Address();
				address.fromJson(jsonAddress.getAsJsonObject());
			}
		}
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
		if (jsonObject.has("logo")) {
			JsonElement jsonLogo = jsonObject.get("logo");
			if (jsonLogo != null) {
				logo = new Resource();
				logo.fromJson(jsonLogo.getAsJsonObject());
			}
		}
		if (jsonObject.has("code")) {
			JsonElement jsonCode = jsonObject.get("code");
			if (jsonCode != null) {
				code = jsonCode.getAsString();
			}
		}
		if (jsonObject.has("description")) {
			JsonElement jsonDescription = jsonObject.get("description");
			if (jsonDescription != null) {
				description = jsonDescription.getAsString();
			}
		}
		if (jsonObject.has("phone")) {
			JsonElement jsonPhone = jsonObject.get("phone");
			if (jsonPhone != null) {
				phone = jsonPhone.getAsString();
			}
		}
		if (jsonObject.has("email")) {
			JsonElement jsonEmail = jsonObject.get("email");
			if (jsonEmail != null) {
				email = jsonEmail.getAsString();
			}
		}
		if (jsonObject.has("invoiceCount")) {
			JsonElement jsonInvoiceCount = jsonObject.get("invoiceCount");
			if (jsonInvoiceCount != null) {
				invoiceCount = Integer.valueOf(jsonInvoiceCount.getAsInt());
			}
		}
		if (jsonObject.has("outstandingCount")) {
			JsonElement jsonOutstandingCount = jsonObject.get("outstandingCount");
			if (jsonOutstandingCount != null) {
				outstandingCount = Integer.valueOf(jsonOutstandingCount.getAsInt());
			}
		}
		if (jsonObject.has("customerCount")) {
			JsonElement jsonCustomerCount = jsonObject.get("customerCount");
			if (jsonCustomerCount != null) {
				customerCount = Integer.valueOf(jsonCustomerCount.getAsInt());
			}
		}
		if (jsonObject.has("accounts")) {
			JsonElement jsonAccounts = jsonObject.get("accounts");
			if (jsonAccounts != null) {
				accounts = new ArrayList<BankAccount>();
				BankAccount item = null;
				for (int i = 0; i < jsonAccounts.getAsJsonArray().size(); i++) {
					if (jsonAccounts.getAsJsonArray().get(i) != null) {
						(item = new BankAccount()).fromJson(jsonAccounts.getAsJsonArray().get(i).getAsJsonObject());
						accounts.add(item);
					}
				}
			}
		}
	}

	public Vendor address(Address address) {
		this.address = address;
		return this;
	}

	public Vendor name(String name) {
		this.name = name;
		return this;
	}

	public Vendor code(String code) {
		this.code = code;
		return this;
	}

	public Vendor description(String description) {
		this.description = description;
		return this;
	}

	public Vendor phone(String phone) {
		this.phone = phone;
		return this;
	}

	public Vendor email(String email) {
		this.email = email;
		return this;
	}

	public Vendor invoiceCount(Integer invoiceCount) {
		this.invoiceCount = invoiceCount;
		return this;
	}

	public Vendor outstandingCount(Integer outstandingCount) {
		this.outstandingCount = outstandingCount;
		return this;
	}

	public Vendor customerCount(Integer customerCount) {
		this.customerCount = customerCount;
		return this;
	}

	public Vendor accounts(List<BankAccount> accounts) {
		this.accounts = accounts;
		return this;
	}
}
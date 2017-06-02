//  
//  BankAccount.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 24, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.shared.api.datatype.invoice;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.willshex.blogwt.shared.api.datatype.DataType;

@Entity
@Cache
public class BankAccount extends DataType {
	@Index public String name;

	public String sortCode;
	public String accountNumber;
	public String bankName;

	@Index public Key<Vendor> vendorKey;
	@Ignore public Vendor vendor;

	@Index(value = IfNotNull.class) public Key<Address> bankAddressKey;
	@Ignore public Address bankAddress;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonName = name == null ? JsonNull.INSTANCE : new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonSortCode = sortCode == null ? JsonNull.INSTANCE : new JsonPrimitive(sortCode);
		object.add("sortCode", jsonSortCode);
		JsonElement jsonVendor = vendor == null ? JsonNull.INSTANCE : vendor.toJson();
		object.add("vendor", jsonVendor);
		JsonElement jsonAccountNumber = accountNumber == null ? JsonNull.INSTANCE : new JsonPrimitive(accountNumber);
		object.add("accountNumber", jsonAccountNumber);
		JsonElement jsonBankName = bankName == null ? JsonNull.INSTANCE : new JsonPrimitive(bankName);
		object.add("bankName", jsonBankName);
		JsonElement jsonBankAddress = bankAddress == null ? JsonNull.INSTANCE : bankAddress.toJson();
		object.add("bankAddress", jsonBankAddress);
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

		if (jsonObject.has("sortCode")) {
			JsonElement jsonSortCode = jsonObject.get("sortCode");
			if (jsonSortCode != null) {
				sortCode = jsonSortCode.getAsString();
			}
		}
		if (jsonObject.has("vendor")) {
			JsonElement jsonVendor = jsonObject.get("vendor");
			if (jsonVendor != null) {
				vendor = new Vendor();
				vendor.fromJson(jsonVendor.getAsJsonObject());
			}
		}
		if (jsonObject.has("accountNumber")) {
			JsonElement jsonAccountNumber = jsonObject.get("accountNumber");
			if (jsonAccountNumber != null) {
				accountNumber = jsonAccountNumber.getAsString();
			}
		}
		if (jsonObject.has("bankName")) {
			JsonElement jsonBankName = jsonObject.get("bankName");
			if (jsonBankName != null) {
				bankName = jsonBankName.getAsString();
			}
		}
		if (jsonObject.has("bankAddress")) {
			JsonElement jsonBankAddress = jsonObject.get("bankAddress");
			if (jsonBankAddress != null) {
				bankAddress = new Address();
				bankAddress.fromJson(jsonBankAddress.getAsJsonObject());
			}
		}
	}

	public BankAccount sortCode(String sortCode) {
		this.sortCode = sortCode;
		return this;
	}

	public BankAccount vendor(Vendor vendor) {
		this.vendor = vendor;
		return this;
	}

	public BankAccount accountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}

	public BankAccount bankName(String bankName) {
		this.bankName = bankName;
		return this;
	}

	public BankAccount bankAddress(Address bankAddress) {
		this.bankAddress = bankAddress;
		return this;
	}
}
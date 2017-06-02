//  
//  Customer.java
//  quickinvoice
//
//  Created by William Shakour on 10 July 2013.
//  Copyrights © 2013 SPACEHOPPER STUDIOS LTD. All rights reserved.
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
import com.willshex.blogwt.shared.api.datatype.DataType;

@Entity
@Cache
public class Customer extends DataType {
	public Key<Address> addressKey;
	@Ignore public Address address;

	@Index public Key<Vendor> vendorKey;
	@Ignore public Vendor vendor;

	@Index public String name;

	@Index public String code;

	public String mainEmail;
	public String secondaryEmail;

	public String description;
	public String phone;
	public Integer invoiceCount;
	public Integer outstandingCount;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonAddress = address == null ? JsonNull.INSTANCE : address.toJson();
		object.add("address", jsonAddress);
		JsonElement jsonVendor = vendor == null ? JsonNull.INSTANCE : vendor.toJson();
		object.add("vendor", jsonVendor);
		JsonElement jsonName = name == null ? JsonNull.INSTANCE : new JsonPrimitive(name);
		object.add("name", jsonName);
		JsonElement jsonCode = code == null ? JsonNull.INSTANCE : new JsonPrimitive(code);
		object.add("code", jsonCode);
		JsonElement jsonInvoiceCount = invoiceCount == null ? JsonNull.INSTANCE : new JsonPrimitive(invoiceCount);
		object.add("invoiceCount", jsonInvoiceCount);
		JsonElement jsonMainEmail = mainEmail == null ? JsonNull.INSTANCE : new JsonPrimitive(mainEmail);
		object.add("mainEmail", jsonMainEmail);
		JsonElement jsonSecondaryEmail = secondaryEmail == null ? JsonNull.INSTANCE : new JsonPrimitive(secondaryEmail);
		object.add("secondaryEmail", jsonSecondaryEmail);
		JsonElement jsonOutstandingCount = outstandingCount == null ? JsonNull.INSTANCE : new JsonPrimitive(outstandingCount);
		object.add("outstandingCount", jsonOutstandingCount);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE : new JsonPrimitive(description);
		object.add("description", jsonDescription);
		JsonElement jsonPhone = phone == null ? JsonNull.INSTANCE : new JsonPrimitive(phone);
		object.add("phone", jsonPhone);
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
		if (jsonObject.has("vendor")) {
			JsonElement jsonVendor = jsonObject.get("vendor");
			if (jsonVendor != null) {
				vendor = new Vendor();
				vendor.fromJson(jsonVendor.getAsJsonObject());
			}
		}
		if (jsonObject.has("name")) {
			JsonElement jsonName = jsonObject.get("name");
			if (jsonName != null) {
				name = jsonName.getAsString();
			}
		}
		if (jsonObject.has("code")) {
			JsonElement jsonCode = jsonObject.get("code");
			if (jsonCode != null) {
				code = jsonCode.getAsString();
			}
		}
		if (jsonObject.has("invoiceCount")) {
			JsonElement jsonInvoiceCount = jsonObject.get("invoiceCount");
			if (jsonInvoiceCount != null) {
				invoiceCount = Integer.valueOf(jsonInvoiceCount.getAsInt());
			}
		}
		if (jsonObject.has("mainEmail")) {
			JsonElement jsonMainEmail = jsonObject.get("mainEmail");
			if (jsonMainEmail != null) {
				mainEmail = jsonMainEmail.getAsString();
			}
		}
		if (jsonObject.has("secondaryEmail")) {
			JsonElement jsonSecondaryEmail = jsonObject.get("secondaryEmail");
			if (jsonSecondaryEmail != null) {
				secondaryEmail = jsonSecondaryEmail.getAsString();
			}
		}
		if (jsonObject.has("outstandingCount")) {
			JsonElement jsonOutstandingCount = jsonObject.get("outstandingCount");
			if (jsonOutstandingCount != null) {
				outstandingCount = Integer.valueOf(jsonOutstandingCount.getAsInt());
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
	}

	public Customer address(Address address) {
		this.address = address;
		return this;
	}

	public Customer vendor(Vendor vendor) {
		this.vendor = vendor;
		return this;
	}

	public Customer name(String name) {
		this.name = name;
		return this;
	}

	public Customer code(String code) {
		this.code = code;
		return this;
	}

	public Customer invoiceCount(Integer invoiceCount) {
		this.invoiceCount = invoiceCount;
		return this;
	}

	public Customer mainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
		return this;
	}

	public Customer secondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
		return this;
	}

	public Customer outstandingCount(Integer outstandingCount) {
		this.outstandingCount = outstandingCount;
		return this;
	}

	public Customer description(String description) {
		this.description = description;
		return this;
	}

	public Customer phone(String phone) {
		this.phone = phone;
		return this;
	}
}
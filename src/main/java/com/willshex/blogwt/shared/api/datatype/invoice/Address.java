//  
//  Address.java
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
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.willshex.blogwt.shared.api.datatype.DataType;

@Entity
@Cache
public class Address extends DataType {
	public String city;
	@Index public String country;
	public String firstLine;
	public String locality;
	@Index public String nameOrNumber;
	@Index public String postalCode;
	public String secondLine;
	public String state;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonCity = city == null ? JsonNull.INSTANCE : new JsonPrimitive(city);
		object.add("city", jsonCity);
		JsonElement jsonCountry = country == null ? JsonNull.INSTANCE : new JsonPrimitive(country);
		object.add("country", jsonCountry);
		JsonElement jsonFirstLine = firstLine == null ? JsonNull.INSTANCE : new JsonPrimitive(firstLine);
		object.add("firstLine", jsonFirstLine);
		JsonElement jsonLocality = locality == null ? JsonNull.INSTANCE : new JsonPrimitive(locality);
		object.add("locality", jsonLocality);
		JsonElement jsonNameOrNumber = nameOrNumber == null ? JsonNull.INSTANCE : new JsonPrimitive(nameOrNumber);
		object.add("nameOrNumber", jsonNameOrNumber);
		JsonElement jsonPostalCode = postalCode == null ? JsonNull.INSTANCE : new JsonPrimitive(postalCode);
		object.add("postalCode", jsonPostalCode);
		JsonElement jsonSecondLine = secondLine == null ? JsonNull.INSTANCE : new JsonPrimitive(secondLine);
		object.add("secondLine", jsonSecondLine);
		JsonElement jsonState = state == null ? JsonNull.INSTANCE : new JsonPrimitive(state);
		object.add("state", jsonState);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("city")) {
			JsonElement jsonCity = jsonObject.get("city");
			if (jsonCity != null) {
				city = jsonCity.getAsString();
			}
		}
		if (jsonObject.has("country")) {
			JsonElement jsonCountry = jsonObject.get("country");
			if (jsonCountry != null) {
				country = jsonCountry.getAsString();
			}
		}
		if (jsonObject.has("firstLine")) {
			JsonElement jsonFirstLine = jsonObject.get("firstLine");
			if (jsonFirstLine != null) {
				firstLine = jsonFirstLine.getAsString();
			}
		}
		if (jsonObject.has("locality")) {
			JsonElement jsonLocality = jsonObject.get("locality");
			if (jsonLocality != null) {
				locality = jsonLocality.getAsString();
			}
		}
		if (jsonObject.has("nameOrNumber")) {
			JsonElement jsonNameOrNumber = jsonObject.get("nameOrNumber");
			if (jsonNameOrNumber != null) {
				nameOrNumber = jsonNameOrNumber.getAsString();
			}
		}
		if (jsonObject.has("postalCode")) {
			JsonElement jsonPostalCode = jsonObject.get("postalCode");
			if (jsonPostalCode != null) {
				postalCode = jsonPostalCode.getAsString();
			}
		}
		if (jsonObject.has("secondLine")) {
			JsonElement jsonSecondLine = jsonObject.get("secondLine");
			if (jsonSecondLine != null) {
				secondLine = jsonSecondLine.getAsString();
			}
		}
		if (jsonObject.has("state")) {
			JsonElement jsonState = jsonObject.get("state");
			if (jsonState != null) {
				state = jsonState.getAsString();
			}
		}
	}

	public Address city(String city) {
		this.city = city;
		return this;
	}

	public Address country(String country) {
		this.country = country;
		return this;
	}

	public Address firstLine(String firstLine) {
		this.firstLine = firstLine;
		return this;
	}

	public Address locality(String locality) {
		this.locality = locality;
		return this;
	}

	public Address nameOrNumber(String nameOrNumber) {
		this.nameOrNumber = nameOrNumber;
		return this;
	}

	public Address postalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public Address secondLine(String secondLine) {
		this.secondLine = secondLine;
		return this;
	}

	public Address state(String state) {
		this.state = state;
		return this;
	}
}
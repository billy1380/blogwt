//  
//  Currency.java
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
public class Currency extends DataType {
	@Index public String code;

	public String description;
	public CurrencyPossitionType possition;
	public String symbol;
	public String symbolMarkup;

	@Override
	public JsonObject toJson() {
		JsonObject object = super.toJson();
		JsonElement jsonCode = code == null ? JsonNull.INSTANCE : new JsonPrimitive(code);
		object.add("code", jsonCode);
		JsonElement jsonDescription = description == null ? JsonNull.INSTANCE : new JsonPrimitive(description);
		object.add("description", jsonDescription);
		JsonElement jsonPossition = possition == null ? JsonNull.INSTANCE : new JsonPrimitive(possition.toString());
		object.add("possition", jsonPossition);
		JsonElement jsonSymbol = symbol == null ? JsonNull.INSTANCE : new JsonPrimitive(symbol);
		object.add("symbol", jsonSymbol);
		JsonElement jsonSymbolMarkup = symbolMarkup == null ? JsonNull.INSTANCE : new JsonPrimitive(symbolMarkup);
		object.add("symbolMarkup", jsonSymbolMarkup);
		return object;
	}

	@Override
	public void fromJson(JsonObject jsonObject) {
		super.fromJson(jsonObject);
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
		if (jsonObject.has("possition")) {
			JsonElement jsonPossition = jsonObject.get("possition");
			if (jsonPossition != null) {
				possition = CurrencyPossitionType.fromString(jsonPossition.getAsString());
			}
		}
		if (jsonObject.has("symbol")) {
			JsonElement jsonSymbol = jsonObject.get("symbol");
			if (jsonSymbol != null) {
				symbol = jsonSymbol.getAsString();
			}
		}
		if (jsonObject.has("symbolMarkup")) {
			JsonElement jsonSymbolMarkup = jsonObject.get("symbolMarkup");
			if (jsonSymbolMarkup != null) {
				symbolMarkup = jsonSymbolMarkup.getAsString();
			}
		}
	}

	public Currency code(String code) {
		this.code = code;
		return this;
	}

	public Currency description(String description) {
		this.description = description;
		return this;
	}

	public Currency possition(CurrencyPossitionType possition) {
		this.possition = possition;
		return this;
	}

	public Currency symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public Currency symbolMarkup(String symbolMarkup) {
		this.symbolMarkup = symbolMarkup;
		return this;
	}
}
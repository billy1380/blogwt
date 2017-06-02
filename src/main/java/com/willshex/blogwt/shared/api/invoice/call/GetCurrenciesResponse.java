//  
//  GetCurrenciesResponse.java
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
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;

public class GetCurrenciesResponse extends Response {
	public List<Currency> currencies;
	public Pager pager;

	@Override
	public JsonObject toJson () {
		JsonObject object = super.toJson();
		JsonElement jsonCurrencies = JsonNull.INSTANCE;
		if (currencies != null) {
			jsonCurrencies = new JsonArray();
			for (int i = 0; i < currencies.size(); i++) {
				JsonElement jsonCurrenciesItem = currencies.get(i) == null
						? JsonNull.INSTANCE : currencies.get(i).toJson();
				((JsonArray) jsonCurrencies).add(jsonCurrenciesItem);
			}
		}
		object.add("currencies", jsonCurrencies);
		JsonElement jsonPager = pager == null ? JsonNull.INSTANCE
				: pager.toJson();
		object.add("pager", jsonPager);
		return object;
	}

	@Override
	public void fromJson (JsonObject jsonObject) {
		super.fromJson(jsonObject);
		if (jsonObject.has("currencies")) {
			JsonElement jsonCurrencies = jsonObject.get("currencies");
			if (jsonCurrencies != null) {
				currencies = new ArrayList<Currency>();
				Currency item = null;
				for (int i = 0; i < jsonCurrencies.getAsJsonArray()
						.size(); i++) {
					if (jsonCurrencies.getAsJsonArray().get(i) != null) {
						(item = new Currency()).fromJson(jsonCurrencies
								.getAsJsonArray().get(i).getAsJsonObject());
						currencies.add(item);
					}
				}
			}
		}

		if (jsonObject.has("pager")) {
			JsonElement jsonPager = jsonObject.get("pager");
			if (jsonPager != null) {
				pager = new Pager();
				pager.fromJson(jsonPager.getAsJsonObject());
			}
		}
	}

	public GetCurrenciesResponse currencies (List<Currency> currencies) {
		this.currencies = currencies;
		return this;
	}

	public GetCurrenciesResponse pager (Pager pager) {
		this.pager = pager;
		return this;
	}
}
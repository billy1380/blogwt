//
//  AddressSuggestOracle.java
//  spchopr.quickinvoice
//
//  Created by William Shakour (billy1380) on 15 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.oracle.invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.willshex.blogwt.client.oracle.SuggestOracle;
import com.willshex.blogwt.client.oracle.invoice.AddressSuggestOracle.AddressSuggestion;
import com.willshex.utility.StringUtils;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddressSuggestOracle extends SuggestOracle<AddressSuggestion> {

	public static class AddressSuggestion {

		protected String id;
		protected String line;
		protected String postcode;

		AddressSuggestion (String id, String line, String postcode) {
			this.id = id;
			this.line = line;
			this.postcode = postcode;
		}
	}

	private com.google.gwt.http.client.Request tracking;

	@Override
	public String getDisplayString (AddressSuggestion item) {
		return item.line;
	}

	@Override
	public String getReplacementString (AddressSuggestion item) {
		return item.postcode;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.SuggestOracle#isDisplayStringHTML() */
	@Override
	public boolean isDisplayStringHTML () {
		return true;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.oracle.SuggestOracle#lookup(com.google.gwt.
	 * user.client.ui.SuggestOracle.Request,
	 * com.google.gwt.user.client.ui.SuggestOracle.Callback) */
	@Override
	protected void lookup (final Request suggestionRequest,
			final Callback callback) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				"http://spchoprcors.appspot.com/api.postcodefinder.royalmail.com/CapturePlus/Interactive/Find/v2.00/json3ex.ws?Key=BH89-YF22-ZU91-EE62&Country=GBR&SearchTerm="
						+ StringUtils.urlencode(suggestionRequest.getQuery())
						+ "&LanguagePreference=en&LastId=&SearchFor=Everything&$block=true&$cache=true");
		try {
			if (tracking != null) {
				tracking.cancel();
				tracking = null;
			}

			this.tracking = builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived (
						com.google.gwt.http.client.Request request,
						com.google.gwt.http.client.Response response) {
					tracking = null;

					String responseText = null;
					if (response.getStatusCode() >= 200
							&& response.getStatusCode() < 300
							&& (responseText = response.getText()) != null
							&& !"".equals(responseText)) {
						List<AddressSuggestion> suggestions = new ArrayList<AddressSuggestion>();

						JsonElement responseJson = (new JsonParser())
								.parse(response.getText());

						JsonObject object;
						JsonArray array;
						AddressSuggestion suggestion;
						String id, text;
						String[] textParts;
						if ((object = responseJson.getAsJsonObject()) != null) {
							if ((array = object.get("Items")
									.getAsJsonArray()) != null) {
								for (JsonElement jsonElement : array) {
									if ((object = jsonElement
											.getAsJsonObject()) != null) {
										id = object.get("Id").getAsString();
										text = object.get("Text").getAsString();
										textParts = text.split(",");
										suggestion = new AddressSuggestion(id,
												text, textParts[0]);
										suggestions.add(suggestion);
									}
								}
							}
						}

						foundItems(suggestionRequest, callback, suggestions);
					} else {
						foundItems(suggestionRequest, callback,
								Collections.<AddressSuggestion> emptyList());
					}
				}

				@Override
				public void onError (com.google.gwt.http.client.Request request,
						Throwable exception) {
					tracking = null;

					foundItems(suggestionRequest, callback,
							Collections.<AddressSuggestion> emptyList());
				}
			});
		} catch (RequestException e) {
			foundItems(suggestionRequest, callback,
					Collections.<AddressSuggestion> emptyList());
		}
	}

}

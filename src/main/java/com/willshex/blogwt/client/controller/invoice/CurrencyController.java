//
//  CurrencyController.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 25 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.controller.invoice;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.event.GetCurrenciesEventHandler.GetCurrenciesFailure;
import com.willshex.blogwt.client.api.invoice.event.GetCurrenciesEventHandler.GetCurrenciesSuccess;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.invoice.ApiHelper;
import com.willshex.blogwt.client.helper.invoice.internal.LookupCache;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CurrencyController extends AsyncDataProvider<Currency> {

	private static CurrencyController one = null;

	public static CurrencyController get () {
		if (one == null) {
			one = new CurrencyController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager();

	/**
	 * 
	 */
	public CurrencyController () {
		String currenciesJson = currencies();

		if (currenciesJson != null) {
			JsonArray jsonCurrencies = (new JsonParser()).parse(currenciesJson)
					.getAsJsonArray();
			Currency item = null;
			for (int i = 0; i < jsonCurrencies.size(); i++) {
				if (jsonCurrencies.get(i).isJsonObject()) {
					(item = new Currency())
							.fromJson(jsonCurrencies.get(i).getAsJsonObject());
					LookupCache.currencies.put(item.code, item);
				}
			}
		}
	}

	private void fetchCurrencies () {
		final GetCurrenciesRequest input = new GetCurrenciesRequest();
		input.accessCode = ApiHelper.ACCESS_CODE;
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		ApiHelper.createInvoiceClient().getCurrencies(input,
				new AsyncCallback<GetCurrenciesResponse>() {

					@Override
					public void onSuccess (GetCurrenciesResponse output) {
						if (output.status != StatusType.StatusTypeFailure) {
							pager = output.pager;
						} else {

				}

						DefaultEventBus.get().fireEventFromSource(
								new GetCurrenciesSuccess(null, output),
								CurrencyController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetCurrenciesFailure(null, caught),
								CurrencyController.this);
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Currency> display) {

	}

	private static native String currencies ()
	/*-{
	return $wnd['currencies'];
	}-*/;
}

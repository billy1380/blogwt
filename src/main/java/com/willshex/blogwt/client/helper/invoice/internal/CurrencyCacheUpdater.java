//
//  CurrencyCacheUpdater.java
//  quickinvoice
//
//  Created by billy1380 on 1 Aug 2013.
//  Copyright © 2013 Spacehopper Studios Ltd. All rights reserved.
//

package com.willshex.blogwt.client.helper.invoice.internal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.datatype.invoice.Currency;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesResponse;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author billy1380
 * 
 */
public class CurrencyCacheUpdater
		implements AsyncCallback<GetCurrenciesResponse> {

	private AsyncCallback<GetCurrenciesResponse> callback = null;

	/**
	 * @param callback
	 */
	public CurrencyCacheUpdater (
			AsyncCallback<GetCurrenciesResponse> callback) {
		this.callback = callback;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.
	 * Throwable ) */
	@Override
	public void onFailure (Throwable caught) {
		if (callback != null) {
			callback.onFailure(caught);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object) */
	@Override
	public void onSuccess (GetCurrenciesResponse result) {
		if (result.status != StatusType.StatusTypeFailure) {
			if (result.currencies != null) {
				for (Currency currency : result.currencies) {
					LookupCache.currencies.put(currency.code, currency);
				}
			}
		}

		if (callback != null) {
			callback.onSuccess(result);
		}
	}

}

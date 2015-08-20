//  
//  Search/SearchService.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.search;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;
import com.willshex.gson.json.service.client.HttpException;
import com.willshex.gson.json.service.client.JsonService;

public final class SearchService extends JsonService {
	public static final String SearchMethodSearchAll = "SearchAll";

	public Request searchAll (final SearchAllRequest input,
			final AsyncCallback<SearchAllResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(SearchMethodSearchAll, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SearchAllResponse outputParameter = new SearchAllResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(SearchService.this,
										SearchMethodSearchAll, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(SearchService.this,
										SearchMethodSearchAll, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(SearchService.this,
									SearchMethodSearchAll, input, exception);
						}
					});
			onCallStart(SearchService.this, SearchMethodSearchAll, input,
					handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(SearchService.this, SearchMethodSearchAll, input,
					exception);
		}
		return handle;
	}
}
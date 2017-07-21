//  
//  Search/SearchService.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.search;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class SearchService extends JsonService {
	public static final String SearchMethodSearchAll = "SearchAll";

	public Request searchAll (SearchAllRequest input) {
		return searchAll(input, null, null);
	}

	public Request searchAll (SearchAllRequest input,
			AsyncSuccess<SearchAllRequest, SearchAllResponse> onSuccess) {
		return searchAll(input, onSuccess, null);
	}

	public Request searchAll (SearchAllRequest input,
			final AsyncCallback<SearchAllResponse> callback) {
		return searchAll(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request searchAll (SearchAllRequest input,
			AsyncSuccess<SearchAllRequest, SearchAllResponse> onSuccess,
			AsyncFailure<SearchAllRequest> onFailure) {
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
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(SearchService.this,
										SearchMethodSearchAll, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(SearchService.this,
										SearchMethodSearchAll, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(SearchService.this,
									SearchMethodSearchAll, input, exception);
						}
					});
			onCallStart(SearchService.this, SearchMethodSearchAll, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(SearchService.this, SearchMethodSearchAll, input,
					exception);
		}
		return handle;
	}
}
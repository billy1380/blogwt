//  
//  Page/PageService.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 25, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.client.api.page;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.gson.json.service.client.HttpException;
import com.willshex.gson.json.service.client.JsonService;

public final class PageService extends JsonService {
	public static final String PageMethodGetPages = "GetPages";

	public Request getPages (final GetPagesRequest input,
			final AsyncCallback<GetPagesResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(PageMethodGetPages, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetPagesResponse outputParameter = new GetPagesResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(PageService.this,
										PageMethodGetPages, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(PageService.this,
										PageMethodGetPages, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(PageService.this, PageMethodGetPages,
									input, exception);
						}
					});
			onCallStart(PageService.this, PageMethodGetPages, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(PageService.this, PageMethodGetPages, input,
					exception);
		}
		return handle;
	}

	public static final String PageMethodGetPage = "GetPage";

	public Request getPage (final GetPageRequest input,
			final AsyncCallback<GetPageResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(PageMethodGetPage, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetPageResponse outputParameter = new GetPageResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(PageService.this,
										PageMethodGetPage, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(PageService.this,
										PageMethodGetPage, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(PageService.this, PageMethodGetPage,
									input, exception);
						}
					});
			onCallStart(PageService.this, PageMethodGetPage, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(PageService.this, PageMethodGetPage, input, exception);
		}
		return handle;
	}
}
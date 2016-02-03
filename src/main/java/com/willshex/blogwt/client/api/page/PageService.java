//  
//  Page/PageService.java
//  blogwt
//
//  Created by William Shakour on June 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.page;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormResponse;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class PageService extends JsonService {
	public static final String PageMethodSubmitForm = "SubmitForm";

	public Request submitForm (final SubmitFormRequest input,
			final AsyncCallback<SubmitFormResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(PageMethodSubmitForm, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SubmitFormResponse outputParameter = new SubmitFormResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(PageService.this,
										PageMethodSubmitForm, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(PageService.this,
										PageMethodSubmitForm, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(PageService.this,
									PageMethodSubmitForm, input, exception);
						}
					});
			onCallStart(PageService.this, PageMethodSubmitForm, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(PageService.this, PageMethodSubmitForm, input,
					exception);
		}
		return handle;
	}

	public static final String PageMethodUpdatePage = "UpdatePage";

	public Request updatePage (final UpdatePageRequest input,
			final AsyncCallback<UpdatePageResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(PageMethodUpdatePage, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								UpdatePageResponse outputParameter = new UpdatePageResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(PageService.this,
										PageMethodUpdatePage, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(PageService.this,
										PageMethodUpdatePage, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(PageService.this,
									PageMethodUpdatePage, input, exception);
						}
					});
			onCallStart(PageService.this, PageMethodUpdatePage, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(PageService.this, PageMethodUpdatePage, input,
					exception);
		}
		return handle;
	}

	public static final String PageMethodDeletePage = "DeletePage";

	public Request deletePage (final DeletePageRequest input,
			final AsyncCallback<DeletePageResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(PageMethodDeletePage, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								DeletePageResponse outputParameter = new DeletePageResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(PageService.this,
										PageMethodDeletePage, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(PageService.this,
										PageMethodDeletePage, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(PageService.this,
									PageMethodDeletePage, input, exception);
						}
					});
			onCallStart(PageService.this, PageMethodDeletePage, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(PageService.this, PageMethodDeletePage, input,
					exception);
		}
		return handle;
	}

	public static final String PageMethodCreatePage = "CreatePage";

	public Request createPage (final CreatePageRequest input,
			final AsyncCallback<CreatePageResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(PageMethodCreatePage, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								CreatePageResponse outputParameter = new CreatePageResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(PageService.this,
										PageMethodCreatePage, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(PageService.this,
										PageMethodCreatePage, input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(PageService.this,
									PageMethodCreatePage, input, exception);
						}
					});
			onCallStart(PageService.this, PageMethodCreatePage, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(PageService.this, PageMethodCreatePage, input,
					exception);
		}
		return handle;
	}

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
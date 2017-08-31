// 
//  Download/DownloadService.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.download;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadRequest;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadResponse;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class DownloadService extends JsonService {
	public static final String DownloadMethodDeleteGeneratedDownloads = "DeleteGeneratedDownloads";

	public Request deleteGeneratedDownloads (
			final DeleteGeneratedDownloadsRequest input,
			final AsyncCallback<DeleteGeneratedDownloadsResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(DownloadMethodDeleteGeneratedDownloads, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								DeleteGeneratedDownloadsResponse outputParameter = new DeleteGeneratedDownloadsResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(DownloadService.this,
										DownloadMethodDeleteGeneratedDownloads,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(DownloadService.this,
										DownloadMethodDeleteGeneratedDownloads,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(DownloadService.this,
									DownloadMethodDeleteGeneratedDownloads,
									input, exception);
						}
					});
			onCallStart(DownloadService.this,
					DownloadMethodDeleteGeneratedDownloads, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(DownloadService.this,
					DownloadMethodDeleteGeneratedDownloads, input, exception);
		}
		return handle;
	}

	public static final String DownloadMethodGenerateDownload = "GenerateDownload";

	public Request generateDownload (final GenerateDownloadRequest input,
			final AsyncCallback<GenerateDownloadResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(DownloadMethodGenerateDownload, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GenerateDownloadResponse outputParameter = new GenerateDownloadResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(DownloadService.this,
										DownloadMethodGenerateDownload, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(DownloadService.this,
										DownloadMethodGenerateDownload, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(DownloadService.this,
									DownloadMethodGenerateDownload, input,
									exception);
						}
					});
			onCallStart(DownloadService.this, DownloadMethodGenerateDownload,
					input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(DownloadService.this, DownloadMethodGenerateDownload,
					input, exception);
		}
		return handle;
	}

	public static final String DownloadMethodGetGeneratedDownloads = "GetGeneratedDownloads";

	public Request getGeneratedDownloads (
			final GetGeneratedDownloadsRequest input,
			final AsyncCallback<GetGeneratedDownloadsResponse> callback) {
		Request handle = null;
		try {
			handle = sendRequest(DownloadMethodGetGeneratedDownloads, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetGeneratedDownloadsResponse outputParameter = new GetGeneratedDownloadsResponse();
								parseResponse(response, outputParameter);
								callback.onSuccess(outputParameter);
								onCallSuccess(DownloadService.this,
										DownloadMethodGetGeneratedDownloads,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								callback.onFailure(exception);
								onCallFailure(DownloadService.this,
										DownloadMethodGetGeneratedDownloads,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							callback.onFailure(exception);
							onCallFailure(DownloadService.this,
									DownloadMethodGetGeneratedDownloads, input,
									exception);
						}
					});
			onCallStart(DownloadService.this,
					DownloadMethodGetGeneratedDownloads, input, handle);
		} catch (RequestException exception) {
			callback.onFailure(exception);
			onCallFailure(DownloadService.this,
					DownloadMethodGetGeneratedDownloads, input, exception);
		}
		return handle;
	}
}
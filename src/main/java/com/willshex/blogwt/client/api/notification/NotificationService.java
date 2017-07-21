// 
//  Notification/NotificationService.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsResponse;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsResponse;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsResponse;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationResponse;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenRequest;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenResponse;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsResponse;
import com.willshex.gson.web.service.client.HttpException;
import com.willshex.gson.web.service.client.JsonService;

public final class NotificationService extends JsonService {
	public static final String NotificationMethodGetNotifications = "GetNotifications";

	public Request getNotifications (GetNotificationsRequest input) {
		return getNotifications(input, null, null);
	}

	public Request getNotifications (GetNotificationsRequest input,
			AsyncSuccess<GetNotificationsRequest, GetNotificationsResponse> onSuccess) {
		return getNotifications(input, onSuccess, null);
	}

	public Request getNotifications (GetNotificationsRequest input,
			final AsyncCallback<GetNotificationsResponse> callback) {
		return getNotifications(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getNotifications (GetNotificationsRequest input,
			AsyncSuccess<GetNotificationsRequest, GetNotificationsResponse> onSuccess,
			AsyncFailure<GetNotificationsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(NotificationMethodGetNotifications, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetNotificationsResponse outputParameter = new GetNotificationsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(NotificationService.this,
										NotificationMethodGetNotifications,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(NotificationService.this,
										NotificationMethodGetNotifications,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(NotificationService.this,
									NotificationMethodGetNotifications, input,
									exception);
						}
					});
			onCallStart(NotificationService.this,
					NotificationMethodGetNotifications, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(NotificationService.this,
					NotificationMethodGetNotifications, input, exception);
		}
		return handle;
	}

	public static final String NotificationMethodGetMetaNotifications = "GetMetaNotifications";

	public Request getMetaNotifications (GetMetaNotificationsRequest input) {
		return getMetaNotifications(input, null, null);
	}

	public Request getMetaNotifications (GetMetaNotificationsRequest input,
			AsyncSuccess<GetMetaNotificationsRequest, GetMetaNotificationsResponse> onSuccess) {
		return getMetaNotifications(input, onSuccess, null);
	}

	public Request getMetaNotifications (GetMetaNotificationsRequest input,
			final AsyncCallback<GetMetaNotificationsResponse> callback) {
		return getMetaNotifications(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getMetaNotifications (GetMetaNotificationsRequest input,
			AsyncSuccess<GetMetaNotificationsRequest, GetMetaNotificationsResponse> onSuccess,
			AsyncFailure<GetMetaNotificationsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(NotificationMethodGetMetaNotifications, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetMetaNotificationsResponse outputParameter = new GetMetaNotificationsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(NotificationService.this,
										NotificationMethodGetMetaNotifications,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(NotificationService.this,
										NotificationMethodGetMetaNotifications,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(NotificationService.this,
									NotificationMethodGetMetaNotifications,
									input, exception);
						}
					});
			onCallStart(NotificationService.this,
					NotificationMethodGetMetaNotifications, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(NotificationService.this,
					NotificationMethodGetMetaNotifications, input, exception);
		}
		return handle;
	}

	public static final String NotificationMethodGetNotificationSettings = "GetNotificationSettings";

	public Request getNotificationSettings (
			GetNotificationSettingsRequest input) {
		return getNotificationSettings(input, null, null);
	}

	public Request getNotificationSettings (
			GetNotificationSettingsRequest input,
			AsyncSuccess<GetNotificationSettingsRequest, GetNotificationSettingsResponse> onSuccess) {
		return getNotificationSettings(input, onSuccess, null);
	}

	public Request getNotificationSettings (
			GetNotificationSettingsRequest input,
			final AsyncCallback<GetNotificationSettingsResponse> callback) {
		return getNotificationSettings(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request getNotificationSettings (
			GetNotificationSettingsRequest input,
			AsyncSuccess<GetNotificationSettingsRequest, GetNotificationSettingsResponse> onSuccess,
			AsyncFailure<GetNotificationSettingsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(NotificationMethodGetNotificationSettings,
					input, new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								GetNotificationSettingsResponse outputParameter = new GetNotificationSettingsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(NotificationService.this,
										NotificationMethodGetNotificationSettings,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(NotificationService.this,
										NotificationMethodGetNotificationSettings,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(NotificationService.this,
									NotificationMethodGetNotificationSettings,
									input, exception);
						}
					});
			onCallStart(NotificationService.this,
					NotificationMethodGetNotificationSettings, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(NotificationService.this,
					NotificationMethodGetNotificationSettings, input,
					exception);
		}
		return handle;
	}

	public static final String NotificationMethodSendAdhocNotification = "SendAdhocNotification";

	public Request sendAdhocNotification (SendAdhocNotificationRequest input) {
		return sendAdhocNotification(input, null, null);
	}

	public Request sendAdhocNotification (SendAdhocNotificationRequest input,
			AsyncSuccess<SendAdhocNotificationRequest, SendAdhocNotificationResponse> onSuccess) {
		return sendAdhocNotification(input, onSuccess, null);
	}

	public Request sendAdhocNotification (SendAdhocNotificationRequest input,
			final AsyncCallback<SendAdhocNotificationResponse> callback) {
		return sendAdhocNotification(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request sendAdhocNotification (SendAdhocNotificationRequest input,
			AsyncSuccess<SendAdhocNotificationRequest, SendAdhocNotificationResponse> onSuccess,
			AsyncFailure<SendAdhocNotificationRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(NotificationMethodSendAdhocNotification, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SendAdhocNotificationResponse outputParameter = new SendAdhocNotificationResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(NotificationService.this,
										NotificationMethodSendAdhocNotification,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(NotificationService.this,
										NotificationMethodSendAdhocNotification,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(NotificationService.this,
									NotificationMethodSendAdhocNotification,
									input, exception);
						}
					});
			onCallStart(NotificationService.this,
					NotificationMethodSendAdhocNotification, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(NotificationService.this,
					NotificationMethodSendAdhocNotification, input, exception);
		}
		return handle;
	}

	public static final String NotificationMethodUpdateNotificationSettings = "UpdateNotificationSettings";

	public Request updateNotificationSettings (
			UpdateNotificationSettingsRequest input) {
		return updateNotificationSettings(input, null, null);
	}

	public Request updateNotificationSettings (
			UpdateNotificationSettingsRequest input,
			AsyncSuccess<UpdateNotificationSettingsRequest, UpdateNotificationSettingsResponse> onSuccess) {
		return updateNotificationSettings(input, onSuccess, null);
	}

	public Request updateNotificationSettings (
			UpdateNotificationSettingsRequest input,
			final AsyncCallback<UpdateNotificationSettingsResponse> callback) {
		return updateNotificationSettings(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request updateNotificationSettings (
			UpdateNotificationSettingsRequest input,
			AsyncSuccess<UpdateNotificationSettingsRequest, UpdateNotificationSettingsResponse> onSuccess,
			AsyncFailure<UpdateNotificationSettingsRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(NotificationMethodUpdateNotificationSettings,
					input, new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								UpdateNotificationSettingsResponse outputParameter = new UpdateNotificationSettingsResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(NotificationService.this,
										NotificationMethodUpdateNotificationSettings,
										input, outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(NotificationService.this,
										NotificationMethodUpdateNotificationSettings,
										input, exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(NotificationService.this,
									NotificationMethodUpdateNotificationSettings,
									input, exception);
						}
					});
			onCallStart(NotificationService.this,
					NotificationMethodUpdateNotificationSettings, input,
					handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(NotificationService.this,
					NotificationMethodUpdateNotificationSettings, input,
					exception);
		}
		return handle;
	}

	public static final String NotificationMethodSetPushToken = "SetPushToken";

	public Request setPushToken (SetPushTokenRequest input) {
		return setPushToken(input, null, null);
	}

	public Request setPushToken (SetPushTokenRequest input,
			AsyncSuccess<SetPushTokenRequest, SetPushTokenResponse> onSuccess) {
		return setPushToken(input, onSuccess, null);
	}

	public Request setPushToken (SetPushTokenRequest input,
			final AsyncCallback<SetPushTokenResponse> callback) {
		return setPushToken(input, (i, o) -> {
			callback.onSuccess(o);
		}, (i, c) -> {
			callback.onFailure(c);
		});
	}

	public Request setPushToken (SetPushTokenRequest input,
			AsyncSuccess<SetPushTokenRequest, SetPushTokenResponse> onSuccess,
			AsyncFailure<SetPushTokenRequest> onFailure) {
		Request handle = null;
		try {
			handle = sendRequest(NotificationMethodSetPushToken, input,
					new RequestCallback() {
						@Override
						public void onResponseReceived (Request request,
								Response response) {
							try {
								SetPushTokenResponse outputParameter = new SetPushTokenResponse();
								parseResponse(response, outputParameter);
								if (onSuccess != null) {
									onSuccess.call(input, outputParameter);
								}

								onCallSuccess(NotificationService.this,
										NotificationMethodSetPushToken, input,
										outputParameter);
							} catch (JSONException | HttpException exception) {
								if (onFailure != null) {
									onFailure.call(input, exception);
								}

								onCallFailure(NotificationService.this,
										NotificationMethodSetPushToken, input,
										exception);
							}
						}

						@Override
						public void onError (Request request,
								Throwable exception) {
							if (onFailure != null) {
								onFailure.call(input, exception);
							}

							onCallFailure(NotificationService.this,
									NotificationMethodSetPushToken, input,
									exception);
						}
					});
			onCallStart(NotificationService.this,
					NotificationMethodSetPushToken, input, handle);
		} catch (RequestException exception) {
			if (onFailure != null) {
				onFailure.call(input, exception);
			}

			onCallFailure(NotificationService.this,
					NotificationMethodSetPushToken, input, exception);
		}
		return handle;
	}
}
// 
//  NotificationJsonServlet.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.notification;

import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.notification.action.AddMetaNotificationActionHandler;
import com.willshex.blogwt.server.api.notification.action.GetMetaNotificationActionHandler;
import com.willshex.blogwt.server.api.notification.action.GetMetaNotificationsActionHandler;
import com.willshex.blogwt.server.api.notification.action.GetNotificationSettingsActionHandler;
import com.willshex.blogwt.server.api.notification.action.GetNotificationsActionHandler;
import com.willshex.blogwt.server.api.notification.action.SendAdhocNotificationActionHandler;
import com.willshex.blogwt.server.api.notification.action.SetPushTokenActionHandler;
import com.willshex.blogwt.server.api.notification.action.UpdateMetaNotificationActionHandler;
import com.willshex.blogwt.server.api.notification.action.UpdateNotificationSettingsActionHandler;
import com.willshex.blogwt.shared.api.notification.Notification;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@WebServlet(name = "Notification API", urlPatterns = Notification.PATH)
public final class NotificationJsonServlet extends JsonServlet {

	@Override
	protected String processAction(String action, JsonObject request) {
		String output = "null";
		if ("UpdateMetaNotification".equals(action)) {
			UpdateMetaNotificationRequest input = new UpdateMetaNotificationRequest();
			input.fromJson(request);
			output = new UpdateMetaNotificationActionHandler().handle(input)
					.toString();
		} else if ("AddMetaNotification".equals(action)) {
			AddMetaNotificationRequest input = new AddMetaNotificationRequest();
			input.fromJson(request);
			output = new AddMetaNotificationActionHandler().handle(input)
					.toString();
		} else if ("GetMetaNotification".equals(action)) {
			GetMetaNotificationRequest input = new GetMetaNotificationRequest();
			input.fromJson(request);
			output = new GetMetaNotificationActionHandler().handle(input)
					.toString();
		} else if ("GetNotifications".equals(action)) {
			GetNotificationsRequest input = new GetNotificationsRequest();
			input.fromJson(request);
			output = new GetNotificationsActionHandler().handle(input)
					.toString();
		} else if ("GetMetaNotifications".equals(action)) {
			GetMetaNotificationsRequest input = new GetMetaNotificationsRequest();
			input.fromJson(request);
			output = new GetMetaNotificationsActionHandler().handle(input)
					.toString();
		} else if ("GetNotificationSettings".equals(action)) {
			GetNotificationSettingsRequest input = new GetNotificationSettingsRequest();
			input.fromJson(request);
			output = new GetNotificationSettingsActionHandler().handle(input)
					.toString();
		} else if ("SendAdhocNotification".equals(action)) {
			SendAdhocNotificationRequest input = new SendAdhocNotificationRequest();
			input.fromJson(request);
			output = new SendAdhocNotificationActionHandler().handle(input)
					.toString();
		} else if ("UpdateNotificationSettings".equals(action)) {
			UpdateNotificationSettingsRequest input = new UpdateNotificationSettingsRequest();
			input.fromJson(request);
			output = new UpdateNotificationSettingsActionHandler().handle(input)
					.toString();
		} else if ("SetPushToken".equals(action)) {
			SetPushTokenRequest input = new SetPushTokenRequest();
			input.fromJson(request);
			output = new SetPushTokenActionHandler().handle(input).toString();
		}
		return output;
	}
}
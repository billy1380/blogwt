// 
//  SendAdhocNotificationEventHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.SendAdhocNotificationResponse;

public interface SendAdhocNotificationEventHandler extends EventHandler {
	public static final GwtEvent.Type<SendAdhocNotificationEventHandler> TYPE = new GwtEvent.Type<SendAdhocNotificationEventHandler>();

	public void sendAdhocNotificationSuccess (
			final SendAdhocNotificationRequest input,
			final SendAdhocNotificationResponse output);

	public void sendAdhocNotificationFailure (
			final SendAdhocNotificationRequest input, final Throwable caught);

	public class SendAdhocNotificationSuccess
			extends GwtEvent<SendAdhocNotificationEventHandler> {
		private SendAdhocNotificationRequest input;
		private SendAdhocNotificationResponse output;

		public SendAdhocNotificationSuccess (
				final SendAdhocNotificationRequest input,
				final SendAdhocNotificationResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SendAdhocNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SendAdhocNotificationEventHandler handler) {
			handler.sendAdhocNotificationSuccess(input, output);
		}
	}

	public class SendAdhocNotificationFailure
			extends GwtEvent<SendAdhocNotificationEventHandler> {
		private SendAdhocNotificationRequest input;
		private Throwable caught;

		public SendAdhocNotificationFailure (
				final SendAdhocNotificationRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SendAdhocNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SendAdhocNotificationEventHandler handler) {
			handler.sendAdhocNotificationFailure(input, caught);
		}
	}

}
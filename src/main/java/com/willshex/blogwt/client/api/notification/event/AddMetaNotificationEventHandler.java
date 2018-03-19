// 
//  AddMetaNotificationEventHandler.java
//  blogwt
// 
//  Created by William Shakour on March 19, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.AddMetaNotificationResponse;

public interface AddMetaNotificationEventHandler extends EventHandler {
	public static final GwtEvent.Type<AddMetaNotificationEventHandler> TYPE = new GwtEvent.Type<AddMetaNotificationEventHandler>();

	public void addMetaNotificationSuccess (
			final AddMetaNotificationRequest input,
			final AddMetaNotificationResponse output);

	public void addMetaNotificationFailure (
			final AddMetaNotificationRequest input, final Throwable caught);

	public class AddMetaNotificationSuccess
			extends GwtEvent<AddMetaNotificationEventHandler> {
		private AddMetaNotificationRequest input;
		private AddMetaNotificationResponse output;

		public AddMetaNotificationSuccess (
				final AddMetaNotificationRequest input,
				final AddMetaNotificationResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<AddMetaNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (AddMetaNotificationEventHandler handler) {
			handler.addMetaNotificationSuccess(input, output);
		}
	}

	public class AddMetaNotificationFailure
			extends GwtEvent<AddMetaNotificationEventHandler> {
		private AddMetaNotificationRequest input;
		private Throwable caught;

		public AddMetaNotificationFailure (
				final AddMetaNotificationRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<AddMetaNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (AddMetaNotificationEventHandler handler) {
			handler.addMetaNotificationFailure(input, caught);
		}
	}

}
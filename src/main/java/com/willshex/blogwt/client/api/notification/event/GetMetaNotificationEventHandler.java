// 
//  GetMetaNotificationEventHandler.java
//  blogwt
// 
//  Created by William Shakour on March 19, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationResponse;

public interface GetMetaNotificationEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetMetaNotificationEventHandler> TYPE = new GwtEvent.Type<GetMetaNotificationEventHandler>();

	public void getMetaNotificationSuccess (
			final GetMetaNotificationRequest input,
			final GetMetaNotificationResponse output);

	public void getMetaNotificationFailure (
			final GetMetaNotificationRequest input, final Throwable caught);

	public class GetMetaNotificationSuccess
			extends GwtEvent<GetMetaNotificationEventHandler> {
		private GetMetaNotificationRequest input;
		private GetMetaNotificationResponse output;

		public GetMetaNotificationSuccess (
				final GetMetaNotificationRequest input,
				final GetMetaNotificationResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetMetaNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetMetaNotificationEventHandler handler) {
			handler.getMetaNotificationSuccess(input, output);
		}
	}

	public class GetMetaNotificationFailure
			extends GwtEvent<GetMetaNotificationEventHandler> {
		private GetMetaNotificationRequest input;
		private Throwable caught;

		public GetMetaNotificationFailure (
				final GetMetaNotificationRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetMetaNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetMetaNotificationEventHandler handler) {
			handler.getMetaNotificationFailure(input, caught);
		}
	}

}
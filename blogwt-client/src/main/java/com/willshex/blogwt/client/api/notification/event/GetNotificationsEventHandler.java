// 
//  GetNotificationsEventHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationsResponse;

public interface GetNotificationsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetNotificationsEventHandler> TYPE = new GwtEvent.Type<GetNotificationsEventHandler>();

	public void getNotificationsSuccess (final GetNotificationsRequest input,
			final GetNotificationsResponse output);

	public void getNotificationsFailure (final GetNotificationsRequest input,
			final Throwable caught);

	public class GetNotificationsSuccess
			extends GwtEvent<GetNotificationsEventHandler> {
		private GetNotificationsRequest input;
		private GetNotificationsResponse output;

		public GetNotificationsSuccess (final GetNotificationsRequest input,
				final GetNotificationsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetNotificationsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetNotificationsEventHandler handler) {
			handler.getNotificationsSuccess(input, output);
		}
	}

	public class GetNotificationsFailure
			extends GwtEvent<GetNotificationsEventHandler> {
		private GetNotificationsRequest input;
		private Throwable caught;

		public GetNotificationsFailure (final GetNotificationsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetNotificationsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetNotificationsEventHandler handler) {
			handler.getNotificationsFailure(input, caught);
		}
	}

}
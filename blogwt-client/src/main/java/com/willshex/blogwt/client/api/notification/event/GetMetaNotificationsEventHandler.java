// 
//  GetMetaNotificationsEventHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetMetaNotificationsResponse;

public interface GetMetaNotificationsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetMetaNotificationsEventHandler> TYPE = new GwtEvent.Type<GetMetaNotificationsEventHandler>();

	public void getMetaNotificationsSuccess (
			final GetMetaNotificationsRequest input,
			final GetMetaNotificationsResponse output);

	public void getMetaNotificationsFailure (
			final GetMetaNotificationsRequest input, final Throwable caught);

	public class GetMetaNotificationsSuccess
			extends GwtEvent<GetMetaNotificationsEventHandler> {
		private GetMetaNotificationsRequest input;
		private GetMetaNotificationsResponse output;

		public GetMetaNotificationsSuccess (
				final GetMetaNotificationsRequest input,
				final GetMetaNotificationsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetMetaNotificationsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetMetaNotificationsEventHandler handler) {
			handler.getMetaNotificationsSuccess(input, output);
		}
	}

	public class GetMetaNotificationsFailure
			extends GwtEvent<GetMetaNotificationsEventHandler> {
		private GetMetaNotificationsRequest input;
		private Throwable caught;

		public GetMetaNotificationsFailure (
				final GetMetaNotificationsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetMetaNotificationsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetMetaNotificationsEventHandler handler) {
			handler.getMetaNotificationsFailure(input, caught);
		}
	}

}
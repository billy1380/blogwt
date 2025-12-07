// 
//  GetNotificationSettingsEventHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.GetNotificationSettingsResponse;

public interface GetNotificationSettingsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetNotificationSettingsEventHandler> TYPE = new GwtEvent.Type<GetNotificationSettingsEventHandler>();

	public void getNotificationSettingsSuccess (
			final GetNotificationSettingsRequest input,
			final GetNotificationSettingsResponse output);

	public void getNotificationSettingsFailure (
			final GetNotificationSettingsRequest input, final Throwable caught);

	public class GetNotificationSettingsSuccess
			extends GwtEvent<GetNotificationSettingsEventHandler> {
		private GetNotificationSettingsRequest input;
		private GetNotificationSettingsResponse output;

		public GetNotificationSettingsSuccess (
				final GetNotificationSettingsRequest input,
				final GetNotificationSettingsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetNotificationSettingsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetNotificationSettingsEventHandler handler) {
			handler.getNotificationSettingsSuccess(input, output);
		}
	}

	public class GetNotificationSettingsFailure
			extends GwtEvent<GetNotificationSettingsEventHandler> {
		private GetNotificationSettingsRequest input;
		private Throwable caught;

		public GetNotificationSettingsFailure (
				final GetNotificationSettingsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetNotificationSettingsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetNotificationSettingsEventHandler handler) {
			handler.getNotificationSettingsFailure(input, caught);
		}
	}

}
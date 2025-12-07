// 
//  UpdateNotificationSettingsEventHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateNotificationSettingsResponse;

public interface UpdateNotificationSettingsEventHandler extends EventHandler {
	public static final GwtEvent.Type<UpdateNotificationSettingsEventHandler> TYPE = new GwtEvent.Type<UpdateNotificationSettingsEventHandler>();

	public void updateNotificationSettingsSuccess (
			final UpdateNotificationSettingsRequest input,
			final UpdateNotificationSettingsResponse output);

	public void updateNotificationSettingsFailure (
			final UpdateNotificationSettingsRequest input,
			final Throwable caught);

	public class UpdateNotificationSettingsSuccess
			extends GwtEvent<UpdateNotificationSettingsEventHandler> {
		private UpdateNotificationSettingsRequest input;
		private UpdateNotificationSettingsResponse output;

		public UpdateNotificationSettingsSuccess (
				final UpdateNotificationSettingsRequest input,
				final UpdateNotificationSettingsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<UpdateNotificationSettingsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (
				UpdateNotificationSettingsEventHandler handler) {
			handler.updateNotificationSettingsSuccess(input, output);
		}
	}

	public class UpdateNotificationSettingsFailure
			extends GwtEvent<UpdateNotificationSettingsEventHandler> {
		private UpdateNotificationSettingsRequest input;
		private Throwable caught;

		public UpdateNotificationSettingsFailure (
				final UpdateNotificationSettingsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<UpdateNotificationSettingsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (
				UpdateNotificationSettingsEventHandler handler) {
			handler.updateNotificationSettingsFailure(input, caught);
		}
	}

}
// 
//  UpdateMetaNotificationEventHandler.java
//  blogwt
// 
//  Created by William Shakour on March 19, 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationRequest;
import com.willshex.blogwt.shared.api.notification.call.UpdateMetaNotificationResponse;

public interface UpdateMetaNotificationEventHandler extends EventHandler {
	public static final GwtEvent.Type<UpdateMetaNotificationEventHandler> TYPE = new GwtEvent.Type<UpdateMetaNotificationEventHandler>();

	public void updateMetaNotificationSuccess (
			final UpdateMetaNotificationRequest input,
			final UpdateMetaNotificationResponse output);

	public void updateMetaNotificationFailure (
			final UpdateMetaNotificationRequest input, final Throwable caught);

	public class UpdateMetaNotificationSuccess
			extends GwtEvent<UpdateMetaNotificationEventHandler> {
		private UpdateMetaNotificationRequest input;
		private UpdateMetaNotificationResponse output;

		public UpdateMetaNotificationSuccess (
				final UpdateMetaNotificationRequest input,
				final UpdateMetaNotificationResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<UpdateMetaNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdateMetaNotificationEventHandler handler) {
			handler.updateMetaNotificationSuccess(input, output);
		}
	}

	public class UpdateMetaNotificationFailure
			extends GwtEvent<UpdateMetaNotificationEventHandler> {
		private UpdateMetaNotificationRequest input;
		private Throwable caught;

		public UpdateMetaNotificationFailure (
				final UpdateMetaNotificationRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<UpdateMetaNotificationEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdateMetaNotificationEventHandler handler) {
			handler.updateMetaNotificationFailure(input, caught);
		}
	}

}
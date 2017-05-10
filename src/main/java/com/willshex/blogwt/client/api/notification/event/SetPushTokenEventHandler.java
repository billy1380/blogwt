// 
//  SetPushTokenEventHandler.java
//  blogwt
// 
//  Created by William Shakour on May 10, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.notification.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenRequest;
import com.willshex.blogwt.shared.api.notification.call.SetPushTokenResponse;

public interface SetPushTokenEventHandler extends EventHandler {
	public static final GwtEvent.Type<SetPushTokenEventHandler> TYPE = new GwtEvent.Type<SetPushTokenEventHandler>();

	public void setPushTokenSuccess (final SetPushTokenRequest input,
			final SetPushTokenResponse output);

	public void setPushTokenFailure (final SetPushTokenRequest input,
			final Throwable caught);

	public class SetPushTokenSuccess
			extends GwtEvent<SetPushTokenEventHandler> {
		private SetPushTokenRequest input;
		private SetPushTokenResponse output;

		public SetPushTokenSuccess (final SetPushTokenRequest input,
				final SetPushTokenResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SetPushTokenEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SetPushTokenEventHandler handler) {
			handler.setPushTokenSuccess(input, output);
		}
	}

	public class SetPushTokenFailure
			extends GwtEvent<SetPushTokenEventHandler> {
		private SetPushTokenRequest input;
		private Throwable caught;

		public SetPushTokenFailure (final SetPushTokenRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SetPushTokenEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SetPushTokenEventHandler handler) {
			handler.setPushTokenFailure(input, caught);
		}
	}

}
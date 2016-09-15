//  
//  ChangePasswordEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ChangePasswordResponse;

public interface ChangePasswordEventHandler extends EventHandler {
	public static final GwtEvent.Type<ChangePasswordEventHandler> TYPE = new GwtEvent.Type<ChangePasswordEventHandler>();

	public void changePasswordSuccess (final ChangePasswordRequest input,
			final ChangePasswordResponse output);

	public void changePasswordFailure (final ChangePasswordRequest input,
			final Throwable caught);

	public class ChangePasswordSuccess extends
			GwtEvent<ChangePasswordEventHandler> {
		private ChangePasswordRequest input;
		private ChangePasswordResponse output;

		public ChangePasswordSuccess (final ChangePasswordRequest input,
				final ChangePasswordResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<ChangePasswordEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangePasswordEventHandler handler) {
			handler.changePasswordSuccess(input, output);
		}
	}

	public class ChangePasswordFailure extends
			GwtEvent<ChangePasswordEventHandler> {
		private ChangePasswordRequest input;
		private Throwable caught;

		public ChangePasswordFailure (final ChangePasswordRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<ChangePasswordEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangePasswordEventHandler handler) {
			handler.changePasswordFailure(input, caught);
		}
	}

}
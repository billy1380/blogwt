//  
//  ResetPasswordEventHandler.java
//  blogwt
//
//  Created by William Shakour on September 6, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ResetPasswordResponse;

public interface ResetPasswordEventHandler extends EventHandler {
	public static final GwtEvent.Type<ResetPasswordEventHandler> TYPE = new GwtEvent.Type<ResetPasswordEventHandler>();

	public void resetPasswordSuccess (final ResetPasswordRequest input,
			final ResetPasswordResponse output);

	public void resetPasswordFailure (final ResetPasswordRequest input,
			final Throwable caught);

	public class ResetPasswordSuccess extends
			GwtEvent<ResetPasswordEventHandler> {
		private ResetPasswordRequest input;
		private ResetPasswordResponse output;

		public ResetPasswordSuccess (final ResetPasswordRequest input,
				final ResetPasswordResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<ResetPasswordEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ResetPasswordEventHandler handler) {
			handler.resetPasswordSuccess(input, output);
		}
	}

	public class ResetPasswordFailure extends
			GwtEvent<ResetPasswordEventHandler> {
		private ResetPasswordRequest input;
		private Throwable caught;

		public ResetPasswordFailure (final ResetPasswordRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<ResetPasswordEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ResetPasswordEventHandler handler) {
			handler.resetPasswordFailure(input, caught);
		}
	}

}
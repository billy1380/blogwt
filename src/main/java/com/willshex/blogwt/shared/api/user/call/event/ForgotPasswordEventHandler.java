//  
//  ForgotPasswordEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordRequest;
import com.willshex.blogwt.shared.api.user.call.ForgotPasswordResponse;

public interface ForgotPasswordEventHandler extends EventHandler {
	public static final GwtEvent.Type<ForgotPasswordEventHandler> TYPE = new GwtEvent.Type<ForgotPasswordEventHandler>();

	public void forgotPasswordSuccess (final ForgotPasswordRequest input,
			final ForgotPasswordResponse output);

	public void forgotPasswordFailure (final ForgotPasswordRequest input,
			final Throwable caught);

	public class ForgotPasswordSuccess extends
			GwtEvent<ForgotPasswordEventHandler> {
		private ForgotPasswordRequest input;
		private ForgotPasswordResponse output;

		public ForgotPasswordSuccess (final ForgotPasswordRequest input,
				final ForgotPasswordResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<ForgotPasswordEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ForgotPasswordEventHandler handler) {
			handler.forgotPasswordSuccess(input, output);
		}
	}

	public class ForgotPasswordFailure extends
			GwtEvent<ForgotPasswordEventHandler> {
		private ForgotPasswordRequest input;
		private Throwable caught;

		public ForgotPasswordFailure (final ForgotPasswordRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<ForgotPasswordEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ForgotPasswordEventHandler handler) {
			handler.forgotPasswordFailure(input, caught);
		}
	}

}
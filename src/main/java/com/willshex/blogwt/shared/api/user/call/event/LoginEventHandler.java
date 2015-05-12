//  
//  LoginEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.LoginRequest;
import com.willshex.blogwt.shared.api.user.call.LoginResponse;

public interface LoginEventHandler extends EventHandler {
	public static final GwtEvent.Type<LoginEventHandler> TYPE = new GwtEvent.Type<LoginEventHandler>();

	public void loginSuccess (final LoginRequest input,
			final LoginResponse output);

	public void loginFailure (final LoginRequest input, final Throwable caught);

	public class LoginSuccess extends GwtEvent<LoginEventHandler> {
		private LoginRequest input;
		private LoginResponse output;

		public LoginSuccess (final LoginRequest input,
				final LoginResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<LoginEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (LoginEventHandler handler) {
			handler.loginSuccess(input, output);
		}
	}

	public class LoginFailure extends GwtEvent<LoginEventHandler> {
		private LoginRequest input;
		private Throwable caught;

		public LoginFailure (final LoginRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<LoginEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (LoginEventHandler handler) {
			handler.loginFailure(input, caught);
		}
	}

}
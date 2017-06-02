//  
//  LoginUserEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 28, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.LoginUserRequest;
import com.willshex.blogwt.shared.api.invoice.call.LoginUserResponse;

public interface LoginUserEventHandler extends EventHandler {
	public static final GwtEvent.Type<LoginUserEventHandler> TYPE = new GwtEvent.Type<LoginUserEventHandler>();

	public void loginUserSuccess(final LoginUserRequest input, final LoginUserResponse output);

	public void loginUserFailure(final LoginUserRequest input, final Throwable caught);

	public class LoginUserSuccess extends GwtEvent<LoginUserEventHandler> {
		private LoginUserRequest input;
		private LoginUserResponse output;

		public LoginUserSuccess(final LoginUserRequest input, final LoginUserResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<LoginUserEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(LoginUserEventHandler handler) {
			handler.loginUserSuccess(input, output);
		}
	}

	public class LoginUserFailure extends GwtEvent<LoginUserEventHandler> {
		private LoginUserRequest input;
		private Throwable caught;

		public LoginUserFailure(final LoginUserRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<LoginUserEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(LoginUserEventHandler handler) {
			handler.loginUserFailure(input, caught);
		}
	}
}
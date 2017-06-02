//  
//  RegisterUserEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 28, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.invoice.call.RegisterUserResponse;

public interface RegisterUserEventHandler extends EventHandler {
	public static final GwtEvent.Type<RegisterUserEventHandler> TYPE = new GwtEvent.Type<RegisterUserEventHandler>();

	public void registerUserSuccess(final RegisterUserRequest input, final RegisterUserResponse output);

	public void registerUserFailure(final RegisterUserRequest input, final Throwable caught);

	public class RegisterUserSuccess extends GwtEvent<RegisterUserEventHandler> {
		private RegisterUserRequest input;
		private RegisterUserResponse output;

		public RegisterUserSuccess(final RegisterUserRequest input, final RegisterUserResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<RegisterUserEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RegisterUserEventHandler handler) {
			handler.registerUserSuccess(input, output);
		}
	}

	public class RegisterUserFailure extends GwtEvent<RegisterUserEventHandler> {
		private RegisterUserRequest input;
		private Throwable caught;

		public RegisterUserFailure(final RegisterUserRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<RegisterUserEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RegisterUserEventHandler handler) {
			handler.registerUserFailure(input, caught);
		}
	}
}
//  
//  RegisterUserEventHandler.java
//  blogwt
//
//  Created by William Shakour on July 18, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.RegisterUserRequest;
import com.willshex.blogwt.shared.api.user.call.RegisterUserResponse;


public interface RegisterUserEventHandler extends EventHandler {
	public static final GwtEvent.Type<RegisterUserEventHandler> TYPE = new GwtEvent.Type<RegisterUserEventHandler>();

	public void registerUserSuccess (final RegisterUserRequest input,
			final RegisterUserResponse output);

	public void registerUserFailure (final RegisterUserRequest input,
			final Throwable caught);

	public class RegisterUserSuccess extends GwtEvent<RegisterUserEventHandler> {
		private RegisterUserRequest input;
		private RegisterUserResponse output;

		public RegisterUserSuccess (final RegisterUserRequest input,
				final RegisterUserResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<RegisterUserEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (RegisterUserEventHandler handler) {
			handler.registerUserSuccess(input, output);
		}
	}

	public class RegisterUserFailure extends GwtEvent<RegisterUserEventHandler> {
		private RegisterUserRequest input;
		private Throwable caught;

		public RegisterUserFailure (final RegisterUserRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<RegisterUserEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (RegisterUserEventHandler handler) {
			handler.registerUserFailure(input, caught);
		}
	}

}
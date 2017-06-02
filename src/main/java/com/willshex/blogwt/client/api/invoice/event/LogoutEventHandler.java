//  
//  LogoutEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 30, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.LogoutRequest;
import com.willshex.blogwt.shared.api.invoice.call.LogoutResponse;

public interface LogoutEventHandler extends EventHandler {
	public static final GwtEvent.Type<LogoutEventHandler> TYPE = new GwtEvent.Type<LogoutEventHandler>();

	public void logoutSuccess(final LogoutRequest input, final LogoutResponse output);

	public void logoutFailure(final LogoutRequest input, final Throwable caught);

	public class LogoutSuccess extends GwtEvent<LogoutEventHandler> {
		private LogoutRequest input;
		private LogoutResponse output;

		public LogoutSuccess(final LogoutRequest input, final LogoutResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<LogoutEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(LogoutEventHandler handler) {
			handler.logoutSuccess(input, output);
		}
	}

	public class LogoutFailure extends GwtEvent<LogoutEventHandler> {
		private LogoutRequest input;
		private Throwable caught;

		public LogoutFailure(final LogoutRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<LogoutEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(LogoutEventHandler handler) {
			handler.logoutFailure(input, caught);
		}
	}

}
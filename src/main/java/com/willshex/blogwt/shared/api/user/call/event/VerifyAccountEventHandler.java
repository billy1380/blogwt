//  
//  VerifyAccountEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on September 6, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountRequest;
import com.willshex.blogwt.shared.api.user.call.VerifyAccountResponse;

public interface VerifyAccountEventHandler extends EventHandler {
	public static final GwtEvent.Type<VerifyAccountEventHandler> TYPE = new GwtEvent.Type<VerifyAccountEventHandler>();

	public void verifyAccountSuccess (final VerifyAccountRequest input,
			final VerifyAccountResponse output);

	public void verifyAccountFailure (final VerifyAccountRequest input,
			final Throwable caught);

	public class VerifyAccountSuccess extends
			GwtEvent<VerifyAccountEventHandler> {
		private VerifyAccountRequest input;
		private VerifyAccountResponse output;

		public VerifyAccountSuccess (final VerifyAccountRequest input,
				final VerifyAccountResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<VerifyAccountEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (VerifyAccountEventHandler handler) {
			handler.verifyAccountSuccess(input, output);
		}
	}

	public class VerifyAccountFailure extends
			GwtEvent<VerifyAccountEventHandler> {
		private VerifyAccountRequest input;
		private Throwable caught;

		public VerifyAccountFailure (final VerifyAccountRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<VerifyAccountEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (VerifyAccountEventHandler handler) {
			handler.verifyAccountFailure(input, caught);
		}
	}

}
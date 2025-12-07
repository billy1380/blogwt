//  
//  ChangeUserDetailsEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserDetailsResponse;

public interface ChangeUserDetailsEventHandler extends EventHandler {
	public static final GwtEvent.Type<ChangeUserDetailsEventHandler> TYPE = new GwtEvent.Type<ChangeUserDetailsEventHandler>();

	public void changeUserDetailsSuccess (final ChangeUserDetailsRequest input,
			final ChangeUserDetailsResponse output);

	public void changeUserDetailsFailure (final ChangeUserDetailsRequest input,
			final Throwable caught);

	public class ChangeUserDetailsSuccess extends
			GwtEvent<ChangeUserDetailsEventHandler> {
		private ChangeUserDetailsRequest input;
		private ChangeUserDetailsResponse output;

		public ChangeUserDetailsSuccess (final ChangeUserDetailsRequest input,
				final ChangeUserDetailsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<ChangeUserDetailsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangeUserDetailsEventHandler handler) {
			handler.changeUserDetailsSuccess(input, output);
		}
	}

	public class ChangeUserDetailsFailure extends
			GwtEvent<ChangeUserDetailsEventHandler> {
		private ChangeUserDetailsRequest input;
		private Throwable caught;

		public ChangeUserDetailsFailure (final ChangeUserDetailsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<ChangeUserDetailsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangeUserDetailsEventHandler handler) {
			handler.changeUserDetailsFailure(input, caught);
		}
	}

}
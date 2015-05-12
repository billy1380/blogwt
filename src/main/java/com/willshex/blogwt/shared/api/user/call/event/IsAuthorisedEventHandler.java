//  
//  IsAuthorisedEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedRequest;
import com.willshex.blogwt.shared.api.user.call.IsAuthorisedResponse;

public interface IsAuthorisedEventHandler extends EventHandler {
	public static final GwtEvent.Type<IsAuthorisedEventHandler> TYPE = new GwtEvent.Type<IsAuthorisedEventHandler>();

	public void isAuthorisedSuccess (final IsAuthorisedRequest input,
			final IsAuthorisedResponse output);

	public void isAuthorisedFailure (final IsAuthorisedRequest input,
			final Throwable caught);

	public class IsAuthorisedSuccess extends GwtEvent<IsAuthorisedEventHandler> {
		private IsAuthorisedRequest input;
		private IsAuthorisedResponse output;

		public IsAuthorisedSuccess (final IsAuthorisedRequest input,
				final IsAuthorisedResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<IsAuthorisedEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (IsAuthorisedEventHandler handler) {
			handler.isAuthorisedSuccess(input, output);
		}
	}

	public class IsAuthorisedFailure extends GwtEvent<IsAuthorisedEventHandler> {
		private IsAuthorisedRequest input;
		private Throwable caught;

		public IsAuthorisedFailure (final IsAuthorisedRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<IsAuthorisedEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (IsAuthorisedEventHandler handler) {
			handler.isAuthorisedFailure(input, caught);
		}
	}

}
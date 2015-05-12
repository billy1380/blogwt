//  
//  GetUserDetailsEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsRequest;
import com.willshex.blogwt.shared.api.user.call.GetUserDetailsResponse;

public interface GetUserDetailsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetUserDetailsEventHandler> TYPE = new GwtEvent.Type<GetUserDetailsEventHandler>();

	public void getUserDetailsSuccess (final GetUserDetailsRequest input,
			final GetUserDetailsResponse output);

	public void getUserDetailsFailure (final GetUserDetailsRequest input,
			final Throwable caught);

	public class GetUserDetailsSuccess extends
			GwtEvent<GetUserDetailsEventHandler> {
		private GetUserDetailsRequest input;
		private GetUserDetailsResponse output;

		public GetUserDetailsSuccess (final GetUserDetailsRequest input,
				final GetUserDetailsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetUserDetailsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetUserDetailsEventHandler handler) {
			handler.getUserDetailsSuccess(input, output);
		}
	}

	public class GetUserDetailsFailure extends
			GwtEvent<GetUserDetailsEventHandler> {
		private GetUserDetailsRequest input;
		private Throwable caught;

		public GetUserDetailsFailure (final GetUserDetailsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetUserDetailsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetUserDetailsEventHandler handler) {
			handler.getUserDetailsFailure(input, caught);
		}
	}

}
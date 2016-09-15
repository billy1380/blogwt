//  
//  GetUsersEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.GetUsersRequest;
import com.willshex.blogwt.shared.api.user.call.GetUsersResponse;

public interface GetUsersEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetUsersEventHandler> TYPE = new GwtEvent.Type<GetUsersEventHandler>();

	public void getUsersSuccess (final GetUsersRequest input,
			final GetUsersResponse output);

	public void getUsersFailure (final GetUsersRequest input,
			final Throwable caught);

	public class GetUsersSuccess extends GwtEvent<GetUsersEventHandler> {
		private GetUsersRequest input;
		private GetUsersResponse output;

		public GetUsersSuccess (final GetUsersRequest input,
				final GetUsersResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetUsersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetUsersEventHandler handler) {
			handler.getUsersSuccess(input, output);
		}
	}

	public class GetUsersFailure extends GwtEvent<GetUsersEventHandler> {
		private GetUsersRequest input;
		private Throwable caught;

		public GetUsersFailure (final GetUsersRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetUsersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetUsersEventHandler handler) {
			handler.getUsersFailure(input, caught);
		}
	}

}
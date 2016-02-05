//  
//  FollowUsersEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.FollowUsersRequest;
import com.willshex.blogwt.shared.api.user.call.FollowUsersResponse;

public interface FollowUsersEventHandler extends EventHandler {
	public static final GwtEvent.Type<FollowUsersEventHandler> TYPE = new GwtEvent.Type<FollowUsersEventHandler>();

	public void followUsersSuccess (final FollowUsersRequest input,
			final FollowUsersResponse output);

	public void followUsersFailure (final FollowUsersRequest input,
			final Throwable caught);

	public class FollowUsersSuccess extends GwtEvent<FollowUsersEventHandler> {
		private FollowUsersRequest input;
		private FollowUsersResponse output;

		public FollowUsersSuccess (final FollowUsersRequest input,
				final FollowUsersResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<FollowUsersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (FollowUsersEventHandler handler) {
			handler.followUsersSuccess(input, output);
		}
	}

	public class FollowUsersFailure extends GwtEvent<FollowUsersEventHandler> {
		private FollowUsersRequest input;
		private Throwable caught;

		public FollowUsersFailure (final FollowUsersRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<FollowUsersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (FollowUsersEventHandler handler) {
			handler.followUsersFailure(input, caught);
		}
	}

}
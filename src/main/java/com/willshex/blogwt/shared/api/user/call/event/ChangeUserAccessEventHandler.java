//  
//  ChangeUserAccessEventHandler.java
//  blogwt
//
//  Created by William Shakour on September 2, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserAccessResponse;

public interface ChangeUserAccessEventHandler extends EventHandler {
	public static final GwtEvent.Type<ChangeUserAccessEventHandler> TYPE = new GwtEvent.Type<ChangeUserAccessEventHandler>();

	public void changeUserAccessSuccess (final ChangeUserAccessRequest input,
			final ChangeUserAccessResponse output);

	public void changeUserAccessFailure (final ChangeUserAccessRequest input,
			final Throwable caught);

	public class ChangeUserAccessSuccess extends
			GwtEvent<ChangeUserAccessEventHandler> {
		private ChangeUserAccessRequest input;
		private ChangeUserAccessResponse output;

		public ChangeUserAccessSuccess (final ChangeUserAccessRequest input,
				final ChangeUserAccessResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<ChangeUserAccessEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangeUserAccessEventHandler handler) {
			handler.changeUserAccessSuccess(input, output);
		}
	}

	public class ChangeUserAccessFailure extends
			GwtEvent<ChangeUserAccessEventHandler> {
		private ChangeUserAccessRequest input;
		private Throwable caught;

		public ChangeUserAccessFailure (final ChangeUserAccessRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<ChangeUserAccessEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangeUserAccessEventHandler handler) {
			handler.changeUserAccessFailure(input, caught);
		}
	}

}
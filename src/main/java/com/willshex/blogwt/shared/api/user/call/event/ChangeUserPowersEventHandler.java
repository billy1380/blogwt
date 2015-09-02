//  
//  ChangeUserPowersEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on September 2, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.ChangeUserPowersRequest;
import com.willshex.blogwt.shared.api.user.call.ChangeUserPowersResponse;

public interface ChangeUserPowersEventHandler extends EventHandler {
	public static final GwtEvent.Type<ChangeUserPowersEventHandler> TYPE = new GwtEvent.Type<ChangeUserPowersEventHandler>();

	public void changeUserPowersSuccess (final ChangeUserPowersRequest input,
			final ChangeUserPowersResponse output);

	public void changeUserPowersFailure (final ChangeUserPowersRequest input,
			final Throwable caught);

	public class ChangeUserPowersSuccess extends
			GwtEvent<ChangeUserPowersEventHandler> {
		private ChangeUserPowersRequest input;
		private ChangeUserPowersResponse output;

		public ChangeUserPowersSuccess (final ChangeUserPowersRequest input,
				final ChangeUserPowersResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<ChangeUserPowersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangeUserPowersEventHandler handler) {
			handler.changeUserPowersSuccess(input, output);
		}
	}

	public class ChangeUserPowersFailure extends
			GwtEvent<ChangeUserPowersEventHandler> {
		private ChangeUserPowersRequest input;
		private Throwable caught;

		public ChangeUserPowersFailure (final ChangeUserPowersRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<ChangeUserPowersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (ChangeUserPowersEventHandler handler) {
			handler.changeUserPowersFailure(input, caught);
		}
	}

}
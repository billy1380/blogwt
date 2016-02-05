//  
//  BlockUsersEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.BlockUsersRequest;
import com.willshex.blogwt.shared.api.user.call.BlockUsersResponse;

public interface BlockUsersEventHandler extends EventHandler {
	public static final GwtEvent.Type<BlockUsersEventHandler> TYPE = new GwtEvent.Type<BlockUsersEventHandler>();

	public void blockUsersSuccess (final BlockUsersRequest input,
			final BlockUsersResponse output);

	public void blockUsersFailure (final BlockUsersRequest input,
			final Throwable caught);

	public class BlockUsersSuccess extends GwtEvent<BlockUsersEventHandler> {
		private BlockUsersRequest input;
		private BlockUsersResponse output;

		public BlockUsersSuccess (final BlockUsersRequest input,
				final BlockUsersResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<BlockUsersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (BlockUsersEventHandler handler) {
			handler.blockUsersSuccess(input, output);
		}
	}

	public class BlockUsersFailure extends GwtEvent<BlockUsersEventHandler> {
		private BlockUsersRequest input;
		private Throwable caught;

		public BlockUsersFailure (final BlockUsersRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<BlockUsersEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (BlockUsersEventHandler handler) {
			handler.blockUsersFailure(input, caught);
		}
	}

}
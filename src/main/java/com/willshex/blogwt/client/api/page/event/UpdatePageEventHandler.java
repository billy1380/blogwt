//  
//  UpdatePageEventHandler.java
//  blogwt
//
//  Created by William Shakour on July 1, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.page.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageResponse;

public interface UpdatePageEventHandler extends EventHandler {
	public static final GwtEvent.Type<UpdatePageEventHandler> TYPE = new GwtEvent.Type<UpdatePageEventHandler>();

	public void updatePageSuccess (final UpdatePageRequest input,
			final UpdatePageResponse output);

	public void updatePageFailure (final UpdatePageRequest input,
			final Throwable caught);

	public class UpdatePageSuccess extends GwtEvent<UpdatePageEventHandler> {
		private UpdatePageRequest input;
		private UpdatePageResponse output;

		public UpdatePageSuccess (final UpdatePageRequest input,
				final UpdatePageResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<UpdatePageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdatePageEventHandler handler) {
			handler.updatePageSuccess(input, output);
		}
	}

	public class UpdatePageFailure extends GwtEvent<UpdatePageEventHandler> {
		private UpdatePageRequest input;
		private Throwable caught;

		public UpdatePageFailure (final UpdatePageRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<UpdatePageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdatePageEventHandler handler) {
			handler.updatePageFailure(input, caught);
		}
	}

}
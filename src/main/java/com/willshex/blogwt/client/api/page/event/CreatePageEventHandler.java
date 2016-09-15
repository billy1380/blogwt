//  
//  DeletePageEventHandler.java
//  blogwt
//
//  Created by William Shakour on June 30, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.page.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;

public interface CreatePageEventHandler extends EventHandler {
	public static final GwtEvent.Type<CreatePageEventHandler> TYPE = new GwtEvent.Type<CreatePageEventHandler>();

	public void createPageSuccess (final CreatePageRequest input,
			final CreatePageResponse output);

	public void createPageFailure (final CreatePageRequest input,
			final Throwable caught);

	public class CreatePageSuccess extends GwtEvent<CreatePageEventHandler> {
		private CreatePageRequest input;
		private CreatePageResponse output;

		public CreatePageSuccess (final CreatePageRequest input,
				final CreatePageResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<CreatePageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (CreatePageEventHandler handler) {
			handler.createPageSuccess(input, output);
		}
	}

	public class CreatePageFailure extends GwtEvent<CreatePageEventHandler> {
		private CreatePageRequest input;
		private Throwable caught;

		public CreatePageFailure (final CreatePageRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<CreatePageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (CreatePageEventHandler handler) {
			handler.createPageFailure(input, caught);
		}
	}

}
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
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;

public interface DeletePageEventHandler extends EventHandler {
	public static final GwtEvent.Type<DeletePageEventHandler> TYPE = new GwtEvent.Type<DeletePageEventHandler>();

	public void deletePageSuccess (final DeletePageRequest input,
			final DeletePageResponse output);

	public void deletePageFailure (final DeletePageRequest input,
			final Throwable caught);

	public class DeletePageSuccess extends GwtEvent<DeletePageEventHandler> {
		private DeletePageRequest input;
		private DeletePageResponse output;

		public DeletePageSuccess (final DeletePageRequest input,
				final DeletePageResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<DeletePageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeletePageEventHandler handler) {
			handler.deletePageSuccess(input, output);
		}
	}

	public class DeletePageFailure extends GwtEvent<DeletePageEventHandler> {
		private DeletePageRequest input;
		private Throwable caught;

		public DeletePageFailure (final DeletePageRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<DeletePageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeletePageEventHandler handler) {
			handler.deletePageFailure(input, caught);
		}
	}

}
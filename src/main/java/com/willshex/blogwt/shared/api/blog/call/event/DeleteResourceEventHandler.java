//  
//  DeleteResourceEventHandler.java
//  blogwt
//
//  Created by William Shakour on August 10, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;

public interface DeleteResourceEventHandler extends EventHandler {
	public static final GwtEvent.Type<DeleteResourceEventHandler> TYPE = new GwtEvent.Type<DeleteResourceEventHandler>();

	public void deleteResourceSuccess (final DeleteResourceRequest input,
			final DeleteResourceResponse output);

	public void deleteResourceFailure (final DeleteResourceRequest input,
			final Throwable caught);

	public class DeleteResourceSuccess extends
			GwtEvent<DeleteResourceEventHandler> {
		private DeleteResourceRequest input;
		private DeleteResourceResponse output;

		public DeleteResourceSuccess (final DeleteResourceRequest input,
				final DeleteResourceResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<DeleteResourceEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeleteResourceEventHandler handler) {
			handler.deleteResourceSuccess(input, output);
		}
	}

	public class DeleteResourceFailure extends
			GwtEvent<DeleteResourceEventHandler> {
		private DeleteResourceRequest input;
		private Throwable caught;

		public DeleteResourceFailure (final DeleteResourceRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<DeleteResourceEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeleteResourceEventHandler handler) {
			handler.deleteResourceFailure(input, caught);
		}
	}

}
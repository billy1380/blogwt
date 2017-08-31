// 
//  DeleteGeneratedDownloadsEventHandler.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.download.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsResponse;

public interface DeleteGeneratedDownloadsEventHandler extends EventHandler {
	public static final GwtEvent.Type<DeleteGeneratedDownloadsEventHandler> TYPE = new GwtEvent.Type<DeleteGeneratedDownloadsEventHandler>();

	public void deleteGeneratedDownloadsSuccess (
			final DeleteGeneratedDownloadsRequest input,
			final DeleteGeneratedDownloadsResponse output);

	public void deleteGeneratedDownloadsFailure (
			final DeleteGeneratedDownloadsRequest input,
			final Throwable caught);

	public class DeleteGeneratedDownloadsSuccess
			extends GwtEvent<DeleteGeneratedDownloadsEventHandler> {
		private DeleteGeneratedDownloadsRequest input;
		private DeleteGeneratedDownloadsResponse output;

		public DeleteGeneratedDownloadsSuccess (
				final DeleteGeneratedDownloadsRequest input,
				final DeleteGeneratedDownloadsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<DeleteGeneratedDownloadsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeleteGeneratedDownloadsEventHandler handler) {
			handler.deleteGeneratedDownloadsSuccess(input, output);
		}
	}

	public class DeleteGeneratedDownloadsFailure
			extends GwtEvent<DeleteGeneratedDownloadsEventHandler> {
		private DeleteGeneratedDownloadsRequest input;
		private Throwable caught;

		public DeleteGeneratedDownloadsFailure (
				final DeleteGeneratedDownloadsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<DeleteGeneratedDownloadsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeleteGeneratedDownloadsEventHandler handler) {
			handler.deleteGeneratedDownloadsFailure(input, caught);
		}
	}

}
// 
//  GetGeneratedDownloadsEventHandler.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.download.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsResponse;

public interface GetGeneratedDownloadsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetGeneratedDownloadsEventHandler> TYPE = new GwtEvent.Type<GetGeneratedDownloadsEventHandler>();

	public void getGeneratedDownloadsSuccess (
			final GetGeneratedDownloadsRequest input,
			final GetGeneratedDownloadsResponse output);

	public void getGeneratedDownloadsFailure (
			final GetGeneratedDownloadsRequest input, final Throwable caught);

	public class GetGeneratedDownloadsSuccess
			extends GwtEvent<GetGeneratedDownloadsEventHandler> {
		private GetGeneratedDownloadsRequest input;
		private GetGeneratedDownloadsResponse output;

		public GetGeneratedDownloadsSuccess (
				final GetGeneratedDownloadsRequest input,
				final GetGeneratedDownloadsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetGeneratedDownloadsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetGeneratedDownloadsEventHandler handler) {
			handler.getGeneratedDownloadsSuccess(input, output);
		}
	}

	public class GetGeneratedDownloadsFailure
			extends GwtEvent<GetGeneratedDownloadsEventHandler> {
		private GetGeneratedDownloadsRequest input;
		private Throwable caught;

		public GetGeneratedDownloadsFailure (
				final GetGeneratedDownloadsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetGeneratedDownloadsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetGeneratedDownloadsEventHandler handler) {
			handler.getGeneratedDownloadsFailure(input, caught);
		}
	}

}
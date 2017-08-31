// 
//  GenerateDownloadEventHandler.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.download.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadRequest;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadResponse;

public interface GenerateDownloadEventHandler extends EventHandler {
	public static final GwtEvent.Type<GenerateDownloadEventHandler> TYPE = new GwtEvent.Type<GenerateDownloadEventHandler>();

	public void generateDownloadSuccess (final GenerateDownloadRequest input,
			final GenerateDownloadResponse output);

	public void generateDownloadFailure (final GenerateDownloadRequest input,
			final Throwable caught);

	public class GenerateDownloadSuccess
			extends GwtEvent<GenerateDownloadEventHandler> {
		private GenerateDownloadRequest input;
		private GenerateDownloadResponse output;

		public GenerateDownloadSuccess (final GenerateDownloadRequest input,
				final GenerateDownloadResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GenerateDownloadEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GenerateDownloadEventHandler handler) {
			handler.generateDownloadSuccess(input, output);
		}
	}

	public class GenerateDownloadFailure
			extends GwtEvent<GenerateDownloadEventHandler> {
		private GenerateDownloadRequest input;
		private Throwable caught;

		public GenerateDownloadFailure (final GenerateDownloadRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GenerateDownloadEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GenerateDownloadEventHandler handler) {
			handler.generateDownloadFailure(input, caught);
		}
	}

}
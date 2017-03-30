//  
//  SubmitRatingEventHandler.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingRequest;
import com.willshex.blogwt.shared.api.blog.call.SubmitRatingResponse;

public interface SubmitRatingEventHandler extends EventHandler {
	public static final GwtEvent.Type<SubmitRatingEventHandler> TYPE = new GwtEvent.Type<SubmitRatingEventHandler>();

	public void submitRatingSuccess (final SubmitRatingRequest input,
			final SubmitRatingResponse output);

	public void submitRatingFailure (final SubmitRatingRequest input,
			final Throwable caught);

	public class SubmitRatingSuccess
			extends GwtEvent<SubmitRatingEventHandler> {
		private SubmitRatingRequest input;
		private SubmitRatingResponse output;

		public SubmitRatingSuccess (final SubmitRatingRequest input,
				final SubmitRatingResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SubmitRatingEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SubmitRatingEventHandler handler) {
			handler.submitRatingSuccess(input, output);
		}
	}

	public class SubmitRatingFailure
			extends GwtEvent<SubmitRatingEventHandler> {
		private SubmitRatingRequest input;
		private Throwable caught;

		public SubmitRatingFailure (final SubmitRatingRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SubmitRatingEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SubmitRatingEventHandler handler) {
			handler.submitRatingFailure(input, caught);
		}
	}

}
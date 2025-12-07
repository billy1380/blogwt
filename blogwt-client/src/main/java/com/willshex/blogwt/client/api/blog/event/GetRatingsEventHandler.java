//  
//  GetRatingsEventHandler.java
//  blogwt
//
//  Created by William Shakour on March 30, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRatingsResponse;

public interface GetRatingsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetRatingsEventHandler> TYPE = new GwtEvent.Type<GetRatingsEventHandler>();

	public void getRatingsSuccess (final GetRatingsRequest input,
			final GetRatingsResponse output);

	public void getRatingsFailure (final GetRatingsRequest input,
			final Throwable caught);

	public class GetRatingsSuccess extends GwtEvent<GetRatingsEventHandler> {
		private GetRatingsRequest input;
		private GetRatingsResponse output;

		public GetRatingsSuccess (final GetRatingsRequest input,
				final GetRatingsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetRatingsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRatingsEventHandler handler) {
			handler.getRatingsSuccess(input, output);
		}
	}

	public class GetRatingsFailure extends GwtEvent<GetRatingsEventHandler> {
		private GetRatingsRequest input;
		private Throwable caught;

		public GetRatingsFailure (final GetRatingsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetRatingsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRatingsEventHandler handler) {
			handler.getRatingsFailure(input, caught);
		}
	}

}
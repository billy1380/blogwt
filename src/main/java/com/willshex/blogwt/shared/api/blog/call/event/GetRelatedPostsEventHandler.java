//  
//  GetRelatedPostsEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 7, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsResponse;

public interface GetRelatedPostsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetRelatedPostsEventHandler> TYPE = new GwtEvent.Type<GetRelatedPostsEventHandler>();

	public void getRelatedPostsSuccess (final GetRelatedPostsRequest input,
			final GetRelatedPostsResponse output);

	public void getRelatedPostsFailure (final GetRelatedPostsRequest input,
			final Throwable caught);

	public class GetRelatedPostsSuccess extends
			GwtEvent<GetRelatedPostsEventHandler> {
		private GetRelatedPostsRequest input;
		private GetRelatedPostsResponse output;

		public GetRelatedPostsSuccess (final GetRelatedPostsRequest input,
				final GetRelatedPostsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetRelatedPostsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRelatedPostsEventHandler handler) {
			handler.getRelatedPostsSuccess(input, output);
		}
	}

	public class GetRelatedPostsFailure extends
			GwtEvent<GetRelatedPostsEventHandler> {
		private GetRelatedPostsRequest input;
		private Throwable caught;

		public GetRelatedPostsFailure (final GetRelatedPostsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetRelatedPostsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRelatedPostsEventHandler handler) {
			handler.getRelatedPostsFailure(input, caught);
		}
	}

}
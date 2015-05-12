//  
//  GetPostsEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;

public interface GetPostsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetPostsEventHandler> TYPE = new GwtEvent.Type<GetPostsEventHandler>();

	public void getPostsSuccess (final GetPostsRequest input,
			final GetPostsResponse output);

	public void getPostsFailure (final GetPostsRequest input,
			final Throwable caught);

	public class GetPostsSuccess extends GwtEvent<GetPostsEventHandler> {
		private GetPostsRequest input;
		private GetPostsResponse output;

		public GetPostsSuccess (final GetPostsRequest input,
				final GetPostsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetPostsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPostsEventHandler handler) {
			handler.getPostsSuccess(input, output);
		}
	}

	public class GetPostsFailure extends GwtEvent<GetPostsEventHandler> {
		private GetPostsRequest input;
		private Throwable caught;

		public GetPostsFailure (final GetPostsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetPostsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPostsEventHandler handler) {
			handler.getPostsFailure(input, caught);
		}
	}

}
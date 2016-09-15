//  
//  GetPostEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;

public interface GetPostEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetPostEventHandler> TYPE = new GwtEvent.Type<GetPostEventHandler>();

	public void getPostSuccess (final GetPostRequest input,
			final GetPostResponse output);

	public void getPostFailure (final GetPostRequest input,
			final Throwable caught);

	public class GetPostSuccess extends GwtEvent<GetPostEventHandler> {
		private GetPostRequest input;
		private GetPostResponse output;

		public GetPostSuccess (final GetPostRequest input,
				final GetPostResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetPostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPostEventHandler handler) {
			handler.getPostSuccess(input, output);
		}
	}

	public class GetPostFailure extends GwtEvent<GetPostEventHandler> {
		private GetPostRequest input;
		private Throwable caught;

		public GetPostFailure (final GetPostRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetPostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPostEventHandler handler) {
			handler.getPostFailure(input, caught);
		}
	}

}
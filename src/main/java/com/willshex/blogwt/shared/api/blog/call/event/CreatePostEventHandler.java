//  
//  CreatePostEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;

public interface CreatePostEventHandler extends EventHandler {
	public static final GwtEvent.Type<CreatePostEventHandler> TYPE = new GwtEvent.Type<CreatePostEventHandler>();

	public void createPostSuccess (final CreatePostRequest input,
			final CreatePostResponse output);

	public void createPostFailure (final CreatePostRequest input,
			final Throwable caught);

	public class CreatePostSuccess extends GwtEvent<CreatePostEventHandler> {
		private CreatePostRequest input;
		private CreatePostResponse output;

		public CreatePostSuccess (final CreatePostRequest input,
				final CreatePostResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<CreatePostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (CreatePostEventHandler handler) {
			handler.createPostSuccess(input, output);
		}
	}

	public class CreatePostFailure extends GwtEvent<CreatePostEventHandler> {
		private CreatePostRequest input;
		private Throwable caught;

		public CreatePostFailure (final CreatePostRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<CreatePostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (CreatePostEventHandler handler) {
			handler.createPostFailure(input, caught);
		}
	}

}
//  
//  UpdatePostEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;

public interface UpdatePostEventHandler extends EventHandler {
	public static final GwtEvent.Type<UpdatePostEventHandler> TYPE = new GwtEvent.Type<UpdatePostEventHandler>();

	public void updatePostSuccess (final UpdatePostRequest input,
			final UpdatePostResponse output);

	public void updatePostFailure (final UpdatePostRequest input,
			final Throwable caught);

	public class UpdatePostSuccess extends GwtEvent<UpdatePostEventHandler> {
		private UpdatePostRequest input;
		private UpdatePostResponse output;

		public UpdatePostSuccess (final UpdatePostRequest input,
				final UpdatePostResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<UpdatePostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdatePostEventHandler handler) {
			handler.updatePostSuccess(input, output);
		}
	}

	public class UpdatePostFailure extends GwtEvent<UpdatePostEventHandler> {
		private UpdatePostRequest input;
		private Throwable caught;

		public UpdatePostFailure (final UpdatePostRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<UpdatePostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdatePostEventHandler handler) {
			handler.updatePostFailure(input, caught);
		}
	}

}
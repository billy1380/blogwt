//  
//  DeletePostEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;

public interface DeletePostEventHandler extends EventHandler {
	public static final GwtEvent.Type<DeletePostEventHandler> TYPE = new GwtEvent.Type<DeletePostEventHandler>();

	public void deletePostSuccess (final DeletePostRequest input,
			final DeletePostResponse output);

	public void deletePostFailure (final DeletePostRequest input,
			final Throwable caught);

	public class DeletePostSuccess extends GwtEvent<DeletePostEventHandler> {
		private DeletePostRequest input;
		private DeletePostResponse output;

		public DeletePostSuccess (final DeletePostRequest input,
				final DeletePostResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<DeletePostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeletePostEventHandler handler) {
			handler.deletePostSuccess(input, output);
		}
	}

	public class DeletePostFailure extends GwtEvent<DeletePostEventHandler> {
		private DeletePostRequest input;
		private Throwable caught;

		public DeletePostFailure (final DeletePostRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<DeletePostEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (DeletePostEventHandler handler) {
			handler.deletePostFailure(input, caught);
		}
	}

}
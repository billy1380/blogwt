//  
//  SetupBlogEventHandler.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;

public interface SetupBlogEventHandler extends EventHandler {
	public static final GwtEvent.Type<SetupBlogEventHandler> TYPE = new GwtEvent.Type<SetupBlogEventHandler>();

	public void setupBlogSuccess (final SetupBlogRequest input,
			final SetupBlogResponse output);

	public void setupBlogFailure (final SetupBlogRequest input,
			final Throwable caught);

	public class SetupBlogSuccess extends GwtEvent<SetupBlogEventHandler> {
		private SetupBlogRequest input;
		private SetupBlogResponse output;

		public SetupBlogSuccess (final SetupBlogRequest input,
				final SetupBlogResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SetupBlogEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SetupBlogEventHandler handler) {
			handler.setupBlogSuccess(input, output);
		}
	}

	public class SetupBlogFailure extends GwtEvent<SetupBlogEventHandler> {
		private SetupBlogRequest input;
		private Throwable caught;

		public SetupBlogFailure (final SetupBlogRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SetupBlogEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (SetupBlogEventHandler handler) {
			handler.setupBlogFailure(input, caught);
		}
	}

}
//  
//  GetArchiveEntriesEventHandler.java
//  blogwt
//
//  Created by William Shakour on August 25, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesResponse;

public interface GetArchiveEntriesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetArchiveEntriesEventHandler> TYPE = new GwtEvent.Type<GetArchiveEntriesEventHandler>();

	public void getArchiveEntriesSuccess (final GetArchiveEntriesRequest input,
			final GetArchiveEntriesResponse output);

	public void getArchiveEntriesFailure (final GetArchiveEntriesRequest input,
			final Throwable caught);

	public class GetArchiveEntriesSuccess extends
			GwtEvent<GetArchiveEntriesEventHandler> {
		private GetArchiveEntriesRequest input;
		private GetArchiveEntriesResponse output;

		public GetArchiveEntriesSuccess (final GetArchiveEntriesRequest input,
				final GetArchiveEntriesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetArchiveEntriesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetArchiveEntriesEventHandler handler) {
			handler.getArchiveEntriesSuccess(input, output);
		}
	}

	public class GetArchiveEntriesFailure extends
			GwtEvent<GetArchiveEntriesEventHandler> {
		private GetArchiveEntriesRequest input;
		private Throwable caught;

		public GetArchiveEntriesFailure (final GetArchiveEntriesRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetArchiveEntriesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetArchiveEntriesEventHandler handler) {
			handler.getArchiveEntriesFailure(input, caught);
		}
	}

}
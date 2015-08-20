//  
//  GetTagsEventHandler.java
//  blogwt
//
//  Created by William Shakour on July 15, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsResponse;

public interface GetTagsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetTagsEventHandler> TYPE = new GwtEvent.Type<GetTagsEventHandler>();

	public void getTagsSuccess (final GetTagsRequest input,
			final GetTagsResponse output);

	public void getTagsFailure (final GetTagsRequest input,
			final Throwable caught);

	public class GetTagsSuccess extends GwtEvent<GetTagsEventHandler> {
		private GetTagsRequest input;
		private GetTagsResponse output;

		public GetTagsSuccess (final GetTagsRequest input,
				final GetTagsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetTagsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetTagsEventHandler handler) {
			handler.getTagsSuccess(input, output);
		}
	}

	public class GetTagsFailure extends GwtEvent<GetTagsEventHandler> {
		private GetTagsRequest input;
		private Throwable caught;

		public GetTagsFailure (final GetTagsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetTagsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetTagsEventHandler handler) {
			handler.getTagsFailure(input, caught);
		}
	}

}
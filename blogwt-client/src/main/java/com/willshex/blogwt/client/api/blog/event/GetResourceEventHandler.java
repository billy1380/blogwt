//  
//  GetResourceEventHandler.java
//  blogwt
//
//  Created by William Shakour on November 1, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceResponse;

public interface GetResourceEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetResourceEventHandler> TYPE = new GwtEvent.Type<GetResourceEventHandler>();

	public void getResourceSuccess (final GetResourceRequest input,
			final GetResourceResponse output);

	public void getResourceFailure (final GetResourceRequest input,
			final Throwable caught);

	public class GetResourceSuccess extends GwtEvent<GetResourceEventHandler> {
		private GetResourceRequest input;
		private GetResourceResponse output;

		public GetResourceSuccess (final GetResourceRequest input,
				final GetResourceResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetResourceEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetResourceEventHandler handler) {
			handler.getResourceSuccess(input, output);
		}
	}

	public class GetResourceFailure extends GwtEvent<GetResourceEventHandler> {
		private GetResourceRequest input;
		private Throwable caught;

		public GetResourceFailure (final GetResourceRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetResourceEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetResourceEventHandler handler) {
			handler.getResourceFailure(input, caught);
		}
	}

}
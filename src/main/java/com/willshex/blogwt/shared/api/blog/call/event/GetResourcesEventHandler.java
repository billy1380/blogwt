//  
//  GetResourcesEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on August 10, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.blog.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;

public interface GetResourcesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetResourcesEventHandler> TYPE = new GwtEvent.Type<GetResourcesEventHandler>();

	public void getResourcesSuccess (final GetResourcesRequest input,
			final GetResourcesResponse output);

	public void getResourcesFailure (final GetResourcesRequest input,
			final Throwable caught);

	public class GetResourcesSuccess extends GwtEvent<GetResourcesEventHandler> {
		private GetResourcesRequest input;
		private GetResourcesResponse output;

		public GetResourcesSuccess (final GetResourcesRequest input,
				final GetResourcesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetResourcesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetResourcesEventHandler handler) {
			handler.getResourcesSuccess(input, output);
		}
	}

	public class GetResourcesFailure extends GwtEvent<GetResourcesEventHandler> {
		private GetResourcesRequest input;
		private Throwable caught;

		public GetResourcesFailure (final GetResourcesRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetResourcesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetResourcesEventHandler handler) {
			handler.getResourcesFailure(input, caught);
		}
	}

}
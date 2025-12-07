// 
//  GetPropertiesEventHandler.java
//  blogwt
// 
//  Created by William Shakour on April 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;

public interface GetPropertiesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetPropertiesEventHandler> TYPE = new GwtEvent.Type<GetPropertiesEventHandler>();

	public void getPropertiesSuccess (final GetPropertiesRequest input,
			final GetPropertiesResponse output);

	public void getPropertiesFailure (final GetPropertiesRequest input,
			final Throwable caught);

	public class GetPropertiesSuccess
			extends GwtEvent<GetPropertiesEventHandler> {
		private GetPropertiesRequest input;
		private GetPropertiesResponse output;

		public GetPropertiesSuccess (final GetPropertiesRequest input,
				final GetPropertiesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetPropertiesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPropertiesEventHandler handler) {
			handler.getPropertiesSuccess(input, output);
		}
	}

	public class GetPropertiesFailure
			extends GwtEvent<GetPropertiesEventHandler> {
		private GetPropertiesRequest input;
		private Throwable caught;

		public GetPropertiesFailure (final GetPropertiesRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetPropertiesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPropertiesEventHandler handler) {
			handler.getPropertiesFailure(input, caught);
		}
	}

}
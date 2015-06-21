//  
//  GetPagesEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;

public interface GetPagesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetPagesEventHandler> TYPE = new GwtEvent.Type<GetPagesEventHandler>();

	public void getPagesSuccess (final GetPagesRequest input,
			final GetPagesResponse output);

	public void getPagesFailure (final GetPagesRequest input,
			final Throwable caught);

	public class GetPagesSuccess extends GwtEvent<GetPagesEventHandler> {
		private GetPagesRequest input;
		private GetPagesResponse output;

		public GetPagesSuccess (final GetPagesRequest input,
				final GetPagesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetPagesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPagesEventHandler handler) {
			handler.getPagesSuccess(input, output);
		}
	}

	public class GetPagesFailure extends GwtEvent<GetPagesEventHandler> {
		private GetPagesRequest input;
		private Throwable caught;

		public GetPagesFailure (final GetPagesRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetPagesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPagesEventHandler handler) {
			handler.getPagesFailure(input, caught);
		}
	}

}
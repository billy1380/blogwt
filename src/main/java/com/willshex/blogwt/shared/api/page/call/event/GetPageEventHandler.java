//  
//  GetPageEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.page.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;

public interface GetPageEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetPageEventHandler> TYPE = new GwtEvent.Type<GetPageEventHandler>();

	public void getPageSuccess (final GetPageRequest input,
			final GetPageResponse output);

	public void getPageFailure (final GetPageRequest input,
			final Throwable caught);

	public class GetPageSuccess extends GwtEvent<GetPageEventHandler> {
		private GetPageRequest input;
		private GetPageResponse output;

		public GetPageSuccess (final GetPageRequest input,
				final GetPageResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetPageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPageEventHandler handler) {
			handler.getPageSuccess(input, output);
		}
	}

	public class GetPageFailure extends GwtEvent<GetPageEventHandler> {
		private GetPageRequest input;
		private Throwable caught;

		public GetPageFailure (final GetPageRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetPageEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPageEventHandler handler) {
			handler.getPageFailure(input, caught);
		}
	}

}
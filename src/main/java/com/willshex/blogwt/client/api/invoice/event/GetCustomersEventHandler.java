//  
//  GetCustomersEventHandler.java
//  quickinvoice
//
//  Created by William Shakour on December 23, 2014.
//  Copyrights © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersResponse;

public interface GetCustomersEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetCustomersEventHandler> TYPE = new GwtEvent.Type<GetCustomersEventHandler>();

	public void getCustomersSuccess(final GetCustomersRequest input, final GetCustomersResponse output);

	public void getCustomersFailure(final GetCustomersRequest input, final Throwable caught);

	public class GetCustomersSuccess extends GwtEvent<GetCustomersEventHandler> {
		private GetCustomersRequest input;
		private GetCustomersResponse output;

		public GetCustomersSuccess(final GetCustomersRequest input, final GetCustomersResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetCustomersEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetCustomersEventHandler handler) {
			handler.getCustomersSuccess(input, output);
		}
	}

	public class GetCustomersFailure extends GwtEvent<GetCustomersEventHandler> {
		private GetCustomersRequest input;
		private Throwable caught;

		public GetCustomersFailure(final GetCustomersRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetCustomersEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetCustomersEventHandler handler) {
			handler.getCustomersFailure(input, caught);
		}
	}

}
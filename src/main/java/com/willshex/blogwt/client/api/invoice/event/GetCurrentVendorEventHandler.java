//  
//  GetCurrentVendorEventHandler.java
//  quickinvoice
//
//  Created by William Shakour on December 23, 2014.
//  Copyrights © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorResponse;

public interface GetCurrentVendorEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetCurrentVendorEventHandler> TYPE = new GwtEvent.Type<GetCurrentVendorEventHandler>();

	public void getCurrentVendorSuccess(final GetCurrentVendorRequest input, final GetCurrentVendorResponse output);

	public void getCurrentVendorFailure(final GetCurrentVendorRequest input, final Throwable caught);

	public class GetCurrentVendorSuccess extends GwtEvent<GetCurrentVendorEventHandler> {
		private GetCurrentVendorRequest input;
		private GetCurrentVendorResponse output;

		public GetCurrentVendorSuccess(final GetCurrentVendorRequest input, final GetCurrentVendorResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetCurrentVendorEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetCurrentVendorEventHandler handler) {
			handler.getCurrentVendorSuccess(input, output);
		}
	}

	public class GetCurrentVendorFailure extends GwtEvent<GetCurrentVendorEventHandler> {
		private GetCurrentVendorRequest input;
		private Throwable caught;

		public GetCurrentVendorFailure(final GetCurrentVendorRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetCurrentVendorEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetCurrentVendorEventHandler handler) {
			handler.getCurrentVendorFailure(input, caught);
		}
	}

}
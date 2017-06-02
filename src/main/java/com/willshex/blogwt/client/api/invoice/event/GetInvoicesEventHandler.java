//  
//  GetInvoicesEventHandler.java
//  quickinvoice
//
//  Created by William Shakour on December 23, 2014.
//  Copyrights © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesResponse;

public interface GetInvoicesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetInvoicesEventHandler> TYPE = new GwtEvent.Type<GetInvoicesEventHandler>();

	public void getInvoicesSuccess(final GetInvoicesRequest input, final GetInvoicesResponse output);

	public void getInvoicesFailure(final GetInvoicesRequest input, final Throwable caught);

	public class GetInvoicesSuccess extends GwtEvent<GetInvoicesEventHandler> {
		private GetInvoicesRequest input;
		private GetInvoicesResponse output;

		public GetInvoicesSuccess(final GetInvoicesRequest input, final GetInvoicesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetInvoicesEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetInvoicesEventHandler handler) {
			handler.getInvoicesSuccess(input, output);
		}
	}

	public class GetInvoicesFailure extends GwtEvent<GetInvoicesEventHandler> {
		private GetInvoicesRequest input;
		private Throwable caught;

		public GetInvoicesFailure(final GetInvoicesRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetInvoicesEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetInvoicesEventHandler handler) {
			handler.getInvoicesFailure(input, caught);
		}
	}

}
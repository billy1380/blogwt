//  
//  GetCurrenciesEventHandler.java
//  quickinvoice
//
//  Created by William Shakour on December 23, 2014.
//  Copyrights © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrenciesResponse;

public interface GetCurrenciesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetCurrenciesEventHandler> TYPE = new GwtEvent.Type<GetCurrenciesEventHandler>();

	public void getCurrenciesSuccess(final GetCurrenciesRequest input, final GetCurrenciesResponse output);

	public void getCurrenciesFailure(final GetCurrenciesRequest input, final Throwable caught);

	public class GetCurrenciesSuccess extends GwtEvent<GetCurrenciesEventHandler> {
		private GetCurrenciesRequest input;
		private GetCurrenciesResponse output;

		public GetCurrenciesSuccess(final GetCurrenciesRequest input, final GetCurrenciesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetCurrenciesEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetCurrenciesEventHandler handler) {
			handler.getCurrenciesSuccess(input, output);
		}
	}

	public class GetCurrenciesFailure extends GwtEvent<GetCurrenciesEventHandler> {
		private GetCurrenciesRequest input;
		private Throwable caught;

		public GetCurrenciesFailure(final GetCurrenciesRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetCurrenciesEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(GetCurrenciesEventHandler handler) {
			handler.getCurrenciesFailure(input, caught);
		}
	}

}
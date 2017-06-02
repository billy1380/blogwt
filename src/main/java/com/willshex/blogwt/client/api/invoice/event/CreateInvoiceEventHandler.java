//  
//  CreateInvoiceEventHandler.java
//  quickinvoice
//
//  Created by William Shakour on December 23, 2014.
//  Copyrights © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceRequest;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceResponse;

public interface CreateInvoiceEventHandler extends EventHandler {
	public static final GwtEvent.Type<CreateInvoiceEventHandler> TYPE = new GwtEvent.Type<CreateInvoiceEventHandler>();

	public void createInvoiceSuccess(final CreateInvoiceRequest input, final CreateInvoiceResponse output);

	public void createInvoiceFailure(final CreateInvoiceRequest input, final Throwable caught);

	public class CreateInvoiceSuccess extends GwtEvent<CreateInvoiceEventHandler> {
		private CreateInvoiceRequest input;
		private CreateInvoiceResponse output;

		public CreateInvoiceSuccess(final CreateInvoiceRequest input, final CreateInvoiceResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<CreateInvoiceEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CreateInvoiceEventHandler handler) {
			handler.createInvoiceSuccess(input, output);
		}
	}

	public class CreateInvoiceFailure extends GwtEvent<CreateInvoiceEventHandler> {
		private CreateInvoiceRequest input;
		private Throwable caught;

		public CreateInvoiceFailure(final CreateInvoiceRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<CreateInvoiceEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CreateInvoiceEventHandler handler) {
			handler.createInvoiceFailure(input, caught);
		}
	}

}
//  
//  SetInvoiceStatusEventHandler.java
//  quickinvoice
//
//  Created by William Shakour on December 23, 2014.
//  Copyrights © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusResponse;

public interface SetInvoiceStatusEventHandler extends EventHandler {
	public static final GwtEvent.Type<SetInvoiceStatusEventHandler> TYPE = new GwtEvent.Type<SetInvoiceStatusEventHandler>();

	public void setInvoiceStatusSuccess(final SetInvoiceStatusRequest input, final SetInvoiceStatusResponse output);

	public void setInvoiceStatusFailure(final SetInvoiceStatusRequest input, final Throwable caught);

	public class SetInvoiceStatusSuccess extends GwtEvent<SetInvoiceStatusEventHandler> {
		private SetInvoiceStatusRequest input;
		private SetInvoiceStatusResponse output;

		public SetInvoiceStatusSuccess(final SetInvoiceStatusRequest input, final SetInvoiceStatusResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SetInvoiceStatusEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(SetInvoiceStatusEventHandler handler) {
			handler.setInvoiceStatusSuccess(input, output);
		}
	}

	public class SetInvoiceStatusFailure extends GwtEvent<SetInvoiceStatusEventHandler> {
		private SetInvoiceStatusRequest input;
		private Throwable caught;

		public SetInvoiceStatusFailure(final SetInvoiceStatusRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SetInvoiceStatusEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(SetInvoiceStatusEventHandler handler) {
			handler.setInvoiceStatusFailure(input, caught);
		}
	}

}
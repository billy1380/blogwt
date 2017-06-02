//  
//  AddCustomerEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 26, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerRequest;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerResponse;

public interface AddCustomerEventHandler extends EventHandler {
	public static final GwtEvent.Type<AddCustomerEventHandler> TYPE = new GwtEvent.Type<AddCustomerEventHandler>();

	public void addCustomerSuccess(final AddCustomerRequest input, final AddCustomerResponse output);

	public void addCustomerFailure(final AddCustomerRequest input, final Throwable caught);

	public class AddCustomerSuccess extends GwtEvent<AddCustomerEventHandler> {
		private AddCustomerRequest input;
		private AddCustomerResponse output;

		public AddCustomerSuccess(final AddCustomerRequest input, final AddCustomerResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<AddCustomerEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(AddCustomerEventHandler handler) {
			handler.addCustomerSuccess(input, output);
		}
	}

	public class AddCustomerFailure extends GwtEvent<AddCustomerEventHandler> {
		private AddCustomerRequest input;
		private Throwable caught;

		public AddCustomerFailure(final AddCustomerRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<AddCustomerEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(AddCustomerEventHandler handler) {
			handler.addCustomerFailure(input, caught);
		}
	}

}
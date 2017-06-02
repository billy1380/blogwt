//  
//  SetupVendorEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 26, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.client.api.invoice.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorResponse;

public interface SetupVendorEventHandler extends EventHandler {
	public static final GwtEvent.Type<SetupVendorEventHandler> TYPE = new GwtEvent.Type<SetupVendorEventHandler>();

	public void setupVendorSuccess(final SetupVendorRequest input, final SetupVendorResponse output);

	public void setupVendorFailure(final SetupVendorRequest input, final Throwable caught);

	public class SetupVendorSuccess extends GwtEvent<SetupVendorEventHandler> {
		private SetupVendorRequest input;
		private SetupVendorResponse output;

		public SetupVendorSuccess(final SetupVendorRequest input, final SetupVendorResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<SetupVendorEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(SetupVendorEventHandler handler) {
			handler.setupVendorSuccess(input, output);
		}
	}

	public class SetupVendorFailure extends GwtEvent<SetupVendorEventHandler> {
		private SetupVendorRequest input;
		private Throwable caught;

		public SetupVendorFailure(final SetupVendorRequest input, final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<SetupVendorEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(SetupVendorEventHandler handler) {
			handler.setupVendorFailure(input, caught);
		}
	}

}
//  
//  GetPermissionsEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetPermissionsResponse;

public interface GetPermissionsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetPermissionsEventHandler> TYPE = new GwtEvent.Type<GetPermissionsEventHandler>();

	public void getPermissionsSuccess (final GetPermissionsRequest input,
			final GetPermissionsResponse output);

	public void getPermissionsFailure (final GetPermissionsRequest input,
			final Throwable caught);

	public class GetPermissionsSuccess extends
			GwtEvent<GetPermissionsEventHandler> {
		private GetPermissionsRequest input;
		private GetPermissionsResponse output;

		public GetPermissionsSuccess (final GetPermissionsRequest input,
				final GetPermissionsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetPermissionsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPermissionsEventHandler handler) {
			handler.getPermissionsSuccess(input, output);
		}
	}

	public class GetPermissionsFailure extends
			GwtEvent<GetPermissionsEventHandler> {
		private GetPermissionsRequest input;
		private Throwable caught;

		public GetPermissionsFailure (final GetPermissionsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetPermissionsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetPermissionsEventHandler handler) {
			handler.getPermissionsFailure(input, caught);
		}
	}

}
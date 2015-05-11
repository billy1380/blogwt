//  
//  GetRolesAndPermissionsEventHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on May 11, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.api.user.call.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesAndPermissionsResponse;

public interface GetRolesAndPermissionsEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetRolesAndPermissionsEventHandler> TYPE = new GwtEvent.Type<GetRolesAndPermissionsEventHandler>();

	public void getRolesAndPermissionsSuccess (
			final GetRolesAndPermissionsRequest input,
			final GetRolesAndPermissionsResponse output);

	public void getRolesAndPermissionsFailure (
			final GetRolesAndPermissionsRequest input, final Throwable caught);

	public class GetRolesAndPermissionsSuccess extends
			GwtEvent<GetRolesAndPermissionsEventHandler> {
		private GetRolesAndPermissionsRequest input;
		private GetRolesAndPermissionsResponse output;

		public GetRolesAndPermissionsSuccess (
				final GetRolesAndPermissionsRequest input,
				final GetRolesAndPermissionsResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetRolesAndPermissionsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRolesAndPermissionsEventHandler handler) {
			handler.getRolesAndPermissionsSuccess(input, output);
		}
	}

	public class GetRolesAndPermissionsFailure extends
			GwtEvent<GetRolesAndPermissionsEventHandler> {
		private GetRolesAndPermissionsRequest input;
		private Throwable caught;

		public GetRolesAndPermissionsFailure (
				final GetRolesAndPermissionsRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetRolesAndPermissionsEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRolesAndPermissionsEventHandler handler) {
			handler.getRolesAndPermissionsFailure(input, caught);
		}
	}

}
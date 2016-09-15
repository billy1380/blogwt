//  
//  GetRolesEventHandler.java
//  blogwt
//
//  Created by William Shakour on June 22, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.GetRolesRequest;
import com.willshex.blogwt.shared.api.user.call.GetRolesResponse;


public interface GetRolesEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetRolesEventHandler> TYPE = new GwtEvent.Type<GetRolesEventHandler>();

	public void getRolesSuccess (final GetRolesRequest input,
			final GetRolesResponse output);

	public void getRolesFailure (final GetRolesRequest input,
			final Throwable caught);

	public class GetRolesSuccess extends GwtEvent<GetRolesEventHandler> {
		private GetRolesRequest input;
		private GetRolesResponse output;

		public GetRolesSuccess (final GetRolesRequest input,
				final GetRolesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetRolesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRolesEventHandler handler) {
			handler.getRolesSuccess(input, output);
		}
	}

	public class GetRolesFailure extends GwtEvent<GetRolesEventHandler> {
		private GetRolesRequest input;
		private Throwable caught;

		public GetRolesFailure (final GetRolesRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetRolesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetRolesEventHandler handler) {
			handler.getRolesFailure(input, caught);
		}
	}

}
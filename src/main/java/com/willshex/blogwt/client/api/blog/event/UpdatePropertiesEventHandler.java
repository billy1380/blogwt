//  
//  UpdatePropertiesEventHandler.java
//  blogwt
//
//  Created by William Shakour on July 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;

public interface UpdatePropertiesEventHandler extends EventHandler {
	public static final GwtEvent.Type<UpdatePropertiesEventHandler> TYPE = new GwtEvent.Type<UpdatePropertiesEventHandler>();

	public void updatePropertiesSuccess (final UpdatePropertiesRequest input,
			final UpdatePropertiesResponse output);

	public void updatePropertiesFailure (final UpdatePropertiesRequest input,
			final Throwable caught);

	public class UpdatePropertiesSuccess extends
			GwtEvent<UpdatePropertiesEventHandler> {
		private UpdatePropertiesRequest input;
		private UpdatePropertiesResponse output;

		public UpdatePropertiesSuccess (final UpdatePropertiesRequest input,
				final UpdatePropertiesResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<UpdatePropertiesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdatePropertiesEventHandler handler) {
			handler.updatePropertiesSuccess(input, output);
		}
	}

	public class UpdatePropertiesFailure extends
			GwtEvent<UpdatePropertiesEventHandler> {
		private UpdatePropertiesRequest input;
		private Throwable caught;

		public UpdatePropertiesFailure (final UpdatePropertiesRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<UpdatePropertiesEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdatePropertiesEventHandler handler) {
			handler.updatePropertiesFailure(input, caught);
		}
	}

}
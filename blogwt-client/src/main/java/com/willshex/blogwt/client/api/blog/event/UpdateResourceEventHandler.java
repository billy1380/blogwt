//  
//  UpdateResourceEventHandler.java
//  blogwt
//
//  Created by William Shakour on November 1, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.blog.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceResponse;

public interface UpdateResourceEventHandler extends EventHandler {
	public static final GwtEvent.Type<UpdateResourceEventHandler> TYPE = new GwtEvent.Type<UpdateResourceEventHandler>();

	public void updateResourceSuccess (final UpdateResourceRequest input,
			final UpdateResourceResponse output);

	public void updateResourceFailure (final UpdateResourceRequest input,
			final Throwable caught);

	public class UpdateResourceSuccess extends
			GwtEvent<UpdateResourceEventHandler> {
		private UpdateResourceRequest input;
		private UpdateResourceResponse output;

		public UpdateResourceSuccess (final UpdateResourceRequest input,
				final UpdateResourceResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<UpdateResourceEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdateResourceEventHandler handler) {
			handler.updateResourceSuccess(input, output);
		}
	}

	public class UpdateResourceFailure extends
			GwtEvent<UpdateResourceEventHandler> {
		private UpdateResourceRequest input;
		private Throwable caught;

		public UpdateResourceFailure (final UpdateResourceRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<UpdateResourceEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (UpdateResourceEventHandler handler) {
			handler.updateResourceFailure(input, caught);
		}
	}

}
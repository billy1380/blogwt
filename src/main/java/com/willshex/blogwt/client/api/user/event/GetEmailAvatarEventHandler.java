//  
//  GetEmailAvatarEventHandler.java
//  blogwt
//
//  Created by William Shakour on August 31, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarRequest;
import com.willshex.blogwt.shared.api.user.call.GetEmailAvatarResponse;

public interface GetEmailAvatarEventHandler extends EventHandler {
	public static final GwtEvent.Type<GetEmailAvatarEventHandler> TYPE = new GwtEvent.Type<GetEmailAvatarEventHandler>();

	public void getEmailAvatarSuccess (final GetEmailAvatarRequest input,
			final GetEmailAvatarResponse output);

	public void getEmailAvatarFailure (final GetEmailAvatarRequest input,
			final Throwable caught);

	public class GetEmailAvatarSuccess extends
			GwtEvent<GetEmailAvatarEventHandler> {
		private GetEmailAvatarRequest input;
		private GetEmailAvatarResponse output;

		public GetEmailAvatarSuccess (final GetEmailAvatarRequest input,
				final GetEmailAvatarResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<GetEmailAvatarEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetEmailAvatarEventHandler handler) {
			handler.getEmailAvatarSuccess(input, output);
		}
	}

	public class GetEmailAvatarFailure extends
			GwtEvent<GetEmailAvatarEventHandler> {
		private GetEmailAvatarRequest input;
		private Throwable caught;

		public GetEmailAvatarFailure (final GetEmailAvatarRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<GetEmailAvatarEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (GetEmailAvatarEventHandler handler) {
			handler.getEmailAvatarFailure(input, caught);
		}
	}

}
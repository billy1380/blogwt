//  
package com.willshex.blogwt.client.api.user.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameRequest;
import com.willshex.blogwt.shared.api.user.call.CheckUsernameResponse;

public interface CheckUsernameEventHandler extends EventHandler {
	public static final GwtEvent.Type<CheckUsernameEventHandler> TYPE = new GwtEvent.Type<CheckUsernameEventHandler>();

	public void checkUsernameSuccess (final CheckUsernameRequest input,
			final CheckUsernameResponse output);

	public void checkUsernameFailure (final CheckUsernameRequest input,
			final Throwable caught);

	public class CheckUsernameSuccess extends
			GwtEvent<CheckUsernameEventHandler> {
		private CheckUsernameRequest input;
		private CheckUsernameResponse output;

		public CheckUsernameSuccess (final CheckUsernameRequest input,
				final CheckUsernameResponse output) {
			this.input = input;
			this.output = output;
		}

		@Override
		public GwtEvent.Type<CheckUsernameEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (CheckUsernameEventHandler handler) {
			handler.checkUsernameSuccess(input, output);
		}
	}

	public class CheckUsernameFailure extends
			GwtEvent<CheckUsernameEventHandler> {
		private CheckUsernameRequest input;
		private Throwable caught;

		public CheckUsernameFailure (final CheckUsernameRequest input,
				final Throwable caught) {
			this.input = input;
			this.caught = caught;
		}

		@Override
		public GwtEvent.Type<CheckUsernameEventHandler> getAssociatedType () {
			return TYPE;
		}

		@Override
		protected void dispatch (CheckUsernameEventHandler handler) {
			handler.checkUsernameFailure(input, caught);
		}
	}

}
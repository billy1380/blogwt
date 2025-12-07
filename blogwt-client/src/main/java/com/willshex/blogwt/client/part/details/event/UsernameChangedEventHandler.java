//
//  UsernameChangedEventHandler.java
//  bidly
//
//  Created by billy1380 on 17 Jul 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.details.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author billy1380
 *
 */
public interface UsernameChangedEventHandler extends EventHandler {
	public static final GwtEvent.Type<UsernameChangedEventHandler> TYPE = new GwtEvent.Type<UsernameChangedEventHandler>();

	public void usernameChanged (String username);

	public static class UsernameChanged
			extends GwtEvent<UsernameChangedEventHandler> {
		String username;

		public UsernameChanged (String username) {
			this.username = username;
		}

		@Override
		public GwtEvent.Type<UsernameChangedEventHandler> getAssociatedType () {
			return UsernameChangedEventHandler.TYPE;
		}

		@Override
		protected void dispatch (UsernameChangedEventHandler handler) {
			handler.usernameChanged(username);
		}
	}

}
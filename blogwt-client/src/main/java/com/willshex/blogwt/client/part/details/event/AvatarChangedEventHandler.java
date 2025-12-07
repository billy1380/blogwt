//
//  AvatarChangedEventHandler.java
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
public interface AvatarChangedEventHandler extends EventHandler {
	public static final GwtEvent.Type<AvatarChangedEventHandler> TYPE = new GwtEvent.Type<AvatarChangedEventHandler>();

	public void avatarChanged (String avatar);

	public static class AvatarChanged
			extends GwtEvent<AvatarChangedEventHandler> {
		String avatar;

		public AvatarChanged (String avatar) {
			this.avatar = avatar;
		}

		@Override
		public GwtEvent.Type<AvatarChangedEventHandler> getAssociatedType () {
			return AvatarChangedEventHandler.TYPE;
		}

		@Override
		protected void dispatch (AvatarChangedEventHandler handler) {
			handler.avatarChanged(avatar);
		}
	}
}

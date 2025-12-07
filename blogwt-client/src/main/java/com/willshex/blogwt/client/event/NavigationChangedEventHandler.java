//
//  NavigationChangedEventHandler.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2014 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
@FunctionalInterface
public interface NavigationChangedEventHandler extends EventHandler {
	public static final GwtEvent.Type<NavigationChangedEventHandler> TYPE = new GwtEvent.Type<NavigationChangedEventHandler>();

	public void navigationChanged (Stack previous, Stack current);

	public class NavigationChangedEvent
			extends GwtEvent<NavigationChangedEventHandler> {

		private Stack stack;
		private Stack previous;

		public NavigationChangedEvent (Stack previous, Stack current) {
			stack = current;
			this.previous = previous;
		}
		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<NavigationChangedEventHandler> getAssociatedType () {
			return TYPE;
		}
		@Override
		protected void dispatch (NavigationChangedEventHandler handler) {
			handler.navigationChanged(previous, stack);
		}
	}
}

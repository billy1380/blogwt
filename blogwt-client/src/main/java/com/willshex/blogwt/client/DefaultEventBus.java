//
//  DefaultEventBus.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2014 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DefaultEventBus {
	private static EventBus eventBus;

	public static EventBus get() {
		if (eventBus == null) {
			eventBus = new SimpleEventBus();
		}

		return eventBus;
	}
}

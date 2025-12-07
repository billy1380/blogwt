//
//  RunAsync.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Jul 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.gwt;

import com.google.gwt.core.client.GWT;

/**
 * @author William Shakour (billy1380)
 *
 */
@FunctionalInterface
public interface RunAsync extends com.google.gwt.core.client.RunAsyncCallback {
	@Override
	default void onFailure (Throwable reason) {
		GWT.log("GWT async runnable failed to load", reason);
	}

	static void run (RunAsync run) {
		GWT.runAsync(run);
	}
}

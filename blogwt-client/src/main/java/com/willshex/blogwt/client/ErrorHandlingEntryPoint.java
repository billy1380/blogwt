//
//  ErrorHandlingEntryPoint.java
//  blogwt
//
//  Created by billy1380 on 4 Aug 2013.
//  Copyright © 2013 WillShex Limited. All rights reserved.
//

package com.willshex.blogwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * @author billy1380
 * 
 */
public abstract class ErrorHandlingEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad () {
		GWT.setUncaughtExceptionHandler(
				e -> GWT.log("An unhandled exception was caugth!", e));
	}
}

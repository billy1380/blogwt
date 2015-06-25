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
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

/**
 * @author billy1380
 * 
 */
public abstract class ErrorHandlingEntryPoint implements EntryPoint {

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad() */
	@Override
	public void onModuleLoad () {
		handleUncaughtExceptions();
	}

	private void handleUncaughtExceptions () {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void onUncaughtException (Throwable e) {
				GWT.log("An unhandled GWT exception was caugth!", e);
			}

			//			private String messageForThrowable(Throwable e) {
			//				String message = "";
			//				if (e.getCause() != null) {
			//					message += "Caused by: " + messageForThrowable(e.getCause());
			//				}
			//
			//				message += e.getMessage() + "\n";
			//
			//				message += "-- Frames --\n";
			//
			//				for (StackTraceElement frame : e.getStackTrace()) {
			//					message += frame.getFileName() + ": " + frame.getClassName() + "." + frame.getMethodName() + " on line " + frame.getLineNumber() + "\n";
			//				}
			//
			//				return message;
			//			}
		});
	}

}

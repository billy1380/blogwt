//  
//  SessionServiceFactory.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.session;

final class SessionServiceFactory {

	/**
	* @return
	*/
	public static ISessionService createNewSessionService () {
		return new SessionService();
	}

}
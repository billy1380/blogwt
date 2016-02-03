//  
//  SessionServiceProvider.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.session;

import com.willshex.service.ServiceDiscovery;

final public class SessionServiceProvider {

	/**
	 * @return
	 */
	public static ISessionService provide () {
		ISessionService sessionService = null;

		if ((sessionService = (ISessionService) ServiceDiscovery
				.getService(ISessionService.NAME)) == null) {
			sessionService = SessionServiceFactory.createNewSessionService();
			ServiceDiscovery.registerService(sessionService);
		}

		return sessionService;
	}

}
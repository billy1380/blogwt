//  
//  PermissionServiceFactory.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.permission;

final class PermissionServiceFactory {

	/**
	 * @return
	 */
	public static IPermissionService createNewPermissionService() {
		return new PermissionService();
	}

}
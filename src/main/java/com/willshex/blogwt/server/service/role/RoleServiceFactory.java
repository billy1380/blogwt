//  
//  RoleServiceFactory.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//  
//
package com.willshex.blogwt.server.service.role;

final class RoleServiceFactory {

	/**
	 * @return
	 */
	public static IRoleService createNewRoleService () {
		return new RoleService();
	}

}
//  
//  RoleServiceProvider.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.role;

import com.spacehopperstudios.service.ServiceDiscovery;

final public class RoleServiceProvider {

	/**
	 * @return
	 */
	public static IRoleService provide () {
		IRoleService roleService = null;

		if ((roleService = (IRoleService) ServiceDiscovery
				.getService(IRoleService.NAME)) == null) {
			roleService = RoleServiceFactory.createNewRoleService();
			ServiceDiscovery.registerService(roleService);
		}

		return roleService;
	}

}
//  
//  PermissionServiceProvider.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.permission;

import com.spacehopperstudios.service.ServiceDiscovery;

final public class PermissionServiceProvider {

	/**
	 * @return
	 */
	public static IPermissionService provide () {
		IPermissionService permissionService = null;

		if ((permissionService = (IPermissionService) ServiceDiscovery
				.getService(IPermissionService.NAME)) == null) {
			permissionService = PermissionServiceFactory
					.createNewPermissionService();
			ServiceDiscovery.registerService(permissionService);
		}

		return permissionService;
	}

}
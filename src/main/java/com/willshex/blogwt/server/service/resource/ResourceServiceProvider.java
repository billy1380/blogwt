//  
//  ResourceServiceProvider.java
//  blogwt
//
//  Created by William Shakour on May 4, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.resource;

import com.spacehopperstudios.service.ServiceDiscovery;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final public class ResourceServiceProvider {

	/**
	 * @return
	 */
	public static IResourceService provide () {
		IResourceService resourceService = null;

		if ((resourceService = (IResourceService) ServiceDiscovery
				.getService(IResourceService.NAME)) == null) {
			resourceService = ResourceServiceFactory.createNewResourceService();
			ServiceDiscovery.registerService(resourceService);
		}

		return resourceService;
	}

}
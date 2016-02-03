//  
//  PropertyServiceProvider.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.property;

import com.willshex.service.ServiceDiscovery;

/**
 * 
 * @author William Shakour (billy1380)
 *
 */
final public class PropertyServiceProvider {

	/**
	 * @return
	 */
	public static IPropertyService provide () {
		IPropertyService propertyService = null;

		if ((propertyService = (IPropertyService) ServiceDiscovery
				.getService(IPropertyService.NAME)) == null) {
			propertyService = PropertyServiceFactory.createNewPropertyService();
			ServiceDiscovery.registerService(propertyService);
		}

		return propertyService;
	}

}
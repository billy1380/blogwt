//  
//  RelationshipServiceProvider.java
//  blogwt
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import com.willshex.service.ServiceDiscovery;

final public class RelationshipServiceProvider {

	/**
	* @return
	*/
	public static IRelationshipService provide () {
		IRelationshipService relationshipService = null;

		if ((relationshipService = (IRelationshipService) ServiceDiscovery
				.getService(IRelationshipService.NAME)) == null) {
			relationshipService = RelationshipServiceFactory
					.createNewRelationshipService();
			ServiceDiscovery.registerService(relationshipService);
		}

		return relationshipService;
	}

}
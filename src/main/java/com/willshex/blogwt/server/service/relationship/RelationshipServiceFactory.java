//  
//  RelationshipServiceFactory.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

final class RelationshipServiceFactory {

	/**
	* @return
	*/
	public static IRelationshipService createNewRelationshipService () {
		return new RelationshipService();
	}

}
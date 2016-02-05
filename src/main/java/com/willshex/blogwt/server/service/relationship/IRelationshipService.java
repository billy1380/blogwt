//  
//  IRelationshipService.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.service.IService;

public interface IRelationshipService extends IService {
	
	public static final String NAME = "blogwt.relationship";
	
	/**
	* @param id
	* @return
	*/
	public Relationship getRelationship (Long id);

	/**
	* @param relationship
	* @return
	*/
	public Relationship addRelationship (Relationship relationship);

	/**
	* @param relationship
	* @return
	*/
	public Relationship updateRelationship (Relationship relationship);

	/**
	* @param relationship
	*/
	public void deleteRelationship (Relationship relationship);

}
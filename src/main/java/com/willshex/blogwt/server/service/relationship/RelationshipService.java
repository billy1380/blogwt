//  
//  RelationshipService.java
//  xsdwsdl2code
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import com.willshex.blogwt.shared.api.datatype.Relationship;

final class RelationshipService implements IRelationshipService {
	public String getName () {
		return NAME;
	}

	@Override
	public Relationship getRelationship (Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Relationship addRelationship (Relationship relationship) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Relationship updateRelationship (Relationship relationship) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteRelationship (Relationship relationship) {
		throw new UnsupportedOperationException();
	}

}
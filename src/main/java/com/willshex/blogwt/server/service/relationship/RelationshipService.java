//  
//  RelationshipService.java
//  blogwt
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.Relationship;

final class RelationshipService implements IRelationshipService {
	public String getName () {
		return NAME;
	}

	@Override
	public Relationship getRelationship (Long id) {
		return ofy().load().type(Relationship.class).id(id.longValue()).now();
	}

	@Override
	public Relationship addRelationship (Relationship relationship) {
		if (relationship.created == null) {
			relationship.created = new Date();
		}

		if (relationship.one != null) {
			relationship.oneKey = Key.create(relationship.one);
		}

		if (relationship.another != null) {
			relationship.anotherKey = Key.create(relationship.another);
		}

		Key<Relationship> key = ofy().save().entity(relationship).now();
		relationship.id = Long.valueOf(key.getId());
		return relationship;
	}

	@Override
	public Relationship updateRelationship (Relationship relationship) {
		if (relationship.one != null) {
			relationship.oneKey = Key.create(relationship.one);
		}

		if (relationship.another != null) {
			relationship.anotherKey = Key.create(relationship.another);
		}

		ofy().save().entity(relationship).now();

		return relationship;
	}

	@Override
	public void deleteRelationship (Relationship relationship) {
		ofy().delete().entity(relationship).now();
	}

}
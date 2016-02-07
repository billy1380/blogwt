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
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;

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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.relationship.IRelationshipService#
	 * deleteUsersRelationship(com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipTypeType) */
	@Override
	public void deleteUsersRelationship (User user, User other,
			RelationshipTypeType type) {
		Relationship relationship = getUsersRelationship(user, other, type);

		if (relationship != null) {
			deleteRelationship(relationship);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.relationship.IRelationshipService#
	 * getUsersRelationship(com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipTypeType) */
	@Override
	public Relationship getUsersRelationship (User user, User other,
			RelationshipTypeType type) {
		return ofy().load().type(Relationship.class).filter("oneKey", user)
				.filter("anotherKey", other).filter("type", type).limit(1)
				.first().now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.relationship.IRelationshipService#
	 * addUsersRelationship(com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipTypeType) */
	@Override
	public Relationship addUsersRelationship (User user, User other,
			RelationshipTypeType type) {
		Relationship relationship = getUsersRelationship(user, other, type);

		if (relationship == null) {
			relationship = new Relationship().one(user).another(other)
					.type(type);

			addRelationship(relationship);
		}

		return relationship;
	}

}
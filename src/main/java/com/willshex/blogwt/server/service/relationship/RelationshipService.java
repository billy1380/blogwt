//  
//  RelationshipService.java
//  blogwt
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.RelationshipSortType;
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;

final class RelationshipService implements IRelationshipService {
	public String getName () {
		return NAME;
	}

	@Override
	public Relationship getRelationship (Long id) {
		return provide().load().type(Relationship.class).id(id.longValue()).now();
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

		Key<Relationship> key = provide().save().entity(relationship).now();
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

		provide().save().entity(relationship).now();

		return relationship;
	}

	@Override
	public void deleteRelationship (Relationship relationship) {
		provide().delete().entity(relationship).now();
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
		return provide().load().type(Relationship.class)
				.filter(RelationshipSortType.RelationshipSortTypeOne.toString()
						+ "Key", user)
				.filter(RelationshipSortType.RelationshipSortTypeAnother
						.toString() + "Key", other)
				.filter(RelationshipSortType.RelationshipSortTypeType
						.toString(), type)
				.limit(1).first().now();
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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.relationship.IRelationshipService#
	 * getUserRelationships(com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipTypeType,java.lang.
	 * Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Relationship> getUserRelationships (User user,
			RelationshipTypeType type, Integer start, Integer count,
			RelationshipSortType sortBy, SortDirectionType sortDirection) {
		Query<Relationship> query = provide().load().type(Relationship.class)
				.filter(RelationshipSortType.RelationshipSortTypeOne.toString()
						+ "Key", user)
				.filter(RelationshipSortType.RelationshipSortTypeType
						.toString(), type.toString());

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		return query.list();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.relationship.IRelationshipService#
	 * getWithUserRelationships(com.willshex.blogwt.shared.api.datatype.User,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipTypeType,
	 * java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.RelationshipSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Relationship> getWithUserRelationships (User user,
			RelationshipTypeType type, Integer start, Integer count,
			RelationshipSortType sortBy, SortDirectionType sortDirection) {
		Query<Relationship> query = provide().load().type(Relationship.class)
				.filter(RelationshipSortType.RelationshipSortTypeAnother
						.toString() + "Key", user)
				.filter(RelationshipSortType.RelationshipSortTypeType
						.toString(), type.toString());

		if (start != null) {
			query = query.offset(start.intValue());
		}

		if (count != null) {
			query = query.limit(count.intValue());
		}

		if (sortBy != null) {
			String condition = sortBy.toString();

			if (sortDirection != null) {
				switch (sortDirection) {
				case SortDirectionTypeDescending:
					condition = "-" + condition;
					break;
				default:
					break;
				}
			}

			query = query.order(condition);
		}

		return query.list();
	}

}
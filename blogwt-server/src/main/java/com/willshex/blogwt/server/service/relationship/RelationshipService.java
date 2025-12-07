//  
//  RelationshipService.java
//  blogwt
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.RelationshipSortType;
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;

final class RelationshipService implements IRelationshipService {
	public String getName() {
		return NAME;
	}

	@Override
	public Relationship getRelationship(Long id) {
		return id(load(), id);
	}

	private LoadType<Relationship> load() {
		return provide().load().type(Relationship.class);
	}

	@Override
	public Relationship addRelationship(Relationship relationship) {
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
		relationship.id = keyToId(key);
		return relationship;
	}

	@Override
	public Relationship updateRelationship(Relationship relationship) {
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
	public void deleteRelationship(Relationship relationship) {
		provide().delete().entity(relationship).now();
	}
	@Override
	public void deleteUsersRelationship(User user, User other,
			RelationshipTypeType type) {
		Relationship relationship = getUsersRelationship(user, other, type);

		if (relationship != null) {
			deleteRelationship(relationship);
		}
	}
	@Override
	public Relationship getUsersRelationship(User user, User other,
			RelationshipTypeType type) {
		return PersistenceHelper.one(load()
				.filter(RelationshipSortType.RelationshipSortTypeOne.toString()
						+ "Key", user)
				.filter(RelationshipSortType.RelationshipSortTypeAnother
						.toString() + "Key", other)
				.filter(RelationshipSortType.RelationshipSortTypeType
						.toString(), type));
	}
	@Override
	public Relationship addUsersRelationship(User user, User other,
			RelationshipTypeType type) {
		Relationship relationship = getUsersRelationship(user, other, type);

		if (relationship == null) {
			relationship = new Relationship().one(user).another(other)
					.type(type);

			addRelationship(relationship);
		}

		return relationship;
	}
	@Override
	public List<Relationship> getUserRelationships(User user,
			RelationshipTypeType type, Integer start, Integer count,
			RelationshipSortType sortBy, SortDirectionType sortDirection) {
		Query<Relationship> query = load()
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
	@Override
	public List<Relationship> getWithUserRelationships(User user,
			RelationshipTypeType type, Integer start, Integer count,
			RelationshipSortType sortBy, SortDirectionType sortDirection) {
		Query<Relationship> query = load()
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

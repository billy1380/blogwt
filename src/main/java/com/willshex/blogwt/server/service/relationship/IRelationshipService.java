//  
//  IRelationshipService.java
//  blogwt
//
//  Created by William Shakour on February 5, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.relationship;

import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.RelationshipSortType;
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;
import com.willshex.blogwt.shared.api.datatype.User;
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

	/**
	 * Delete users relationship
	 * @param user
	 * @param other
	 * @param type
	 */
	public void deleteUsersRelationship (User user, User other,
			RelationshipTypeType type);

	/**
	 * Add users relationship
	 * @param user
	 * @param other
	 * @param type
	 * @return
	 */
	public Relationship addUsersRelationship (User user, User other,
			RelationshipTypeType type);

	/**
	 * Get users relationship
	 * @param user
	 * @param other
	 * @param type
	 * @return
	 */
	public Relationship getUsersRelationship (User user, User other,
			RelationshipTypeType type);

	/**
	 * Get user relationships
	 * @param user
	 * @param type
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Relationship> getUserRelationships (User user,
			RelationshipTypeType type, Integer start, Integer count,
			RelationshipSortType sortBy, SortDirectionType sortDirection);

	/**
	 * Get with user relationships
	 * @param user
	 * @param type
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Relationship> getWithUserRelationships (User user,
			RelationshipTypeType type, Integer start, Integer count,
			RelationshipSortType sortBy, SortDirectionType sortDirection);

}
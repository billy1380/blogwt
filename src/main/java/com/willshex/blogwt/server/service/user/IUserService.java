//  
//  IUserService.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

import java.util.Collection;
import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;

public interface IUserService extends IService {
	public static final String NAME = "blogwt.user";

	/**
	 * @param id
	 * @return
	 */
	public User getUser (Long id);

	/**
	 * @param user
	 * @return
	 */
	public User addUser (User user);

	/**
	 * @param user
	 * @return
	 */
	public User updateUser (User user);

	/**
	 * @param user
	 */
	public void deleteUser (User user);

	/**
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<User> getUsers (Integer start, Integer count,
			UserSortType sortBy, SortDirectionType sortDirection);

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public User getLoginUser (String username, String password);

	/**
	 * @param userId
	 */
	public void updateUserIdLastLoggedIn (Long userId);

	/**
	 * 
	 * @param users
	 */
	public void addUserBatch (Collection<User> users);

	/**
	 * @param username
	 * @return
	 */
	public User getUsernameUser (String username);

	/**
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public Boolean verifyPassword (User user, String password);

	/**
	 * 
	 * @param password
	 * @return
	 */
	public String generatePassword (String password);

	/**
	 * Add user roles and permissions
	 * @param user
	 * @param roles
	 * @param permissions
	 * @return
	 */
	public User addUserRolesAndPermissions (User user, List<Role> roles,
			List<Permission> permissions);

	/**
	 * Remove user roles and permissions
	 * @param user
	 * @param roles
	 * @param permissions
	 * @return
	 */
	public User removeUserRolesAndPermissions (User user, List<Role> roles,
			List<Permission> permissions);

	/**
	 * Reset user password
	 * @param user
	 */
	public void resetPassword (User user);

	/**
	 * Verify account
	 * @param user
	 */
	public void verifyAccount (User user);

	/**
	 * @return
	 */
	public User getActionCodeUser (String actionCode);

	/**
	 * @param email
	 * @return
	 */
	public User getEmailUser (String email);
	
	/**
	 * Index all
	 */
	public void indexAll();
}
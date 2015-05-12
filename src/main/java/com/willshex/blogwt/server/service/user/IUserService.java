//  
//  IUserService.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

import java.util.List;

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.SortDirectionType;
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
	public List<User> getUsers (Long start, Long count, UserSortType sortBy,
			SortDirectionType sortDirection);

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

}
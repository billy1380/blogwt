//  
//  UserService.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;

final class UserService implements IUserService {

	private static final String SALT = "af1d3250-f8d1-11e4-bbd2-7054d251af02";

	public String getName () {
		return NAME;
	}

	public User getUser (Long id) {
		return addAvatar(ofy().load().type(User.class).id(id.longValue()).now());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.user.IUserService#addUser
	 * (com.spacehopperstudios.blogwt.shared.api.datatypes.User) */
	@Override
	public User addUser (User user) {
		if (user.created == null) {
			user.added = user.created = new Date();
		}

		user.password = StringUtils.sha1Hash(user.password + SALT);

		Key<User> key = ofy().save().entity(user).now();
		user.id = Long.valueOf(key.getId());

		return user;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.user.IUserService#updateUser
	 * (com.spacehopperstudios.blogwt.shared.api.datatypes.User) */
	@Override
	public User updateUser (User user) {
		ofy().save().entity(user).now();
		return user;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.user.IUserService#deleteUser
	 * (com.spacehopperstudios.blogwt.shared.api.datatypes.User) */
	@Override
	public void deleteUser (User user) {
		ofy().delete().entity(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.user.IUserService#getUsers
	 * (java.lang.Long, java.lang.Long,
	 * com.spacehopperstudios.blogwt.shared.api.datatypes.UserSortType,
	 * com.spacehopperstudios.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<User> getUsers (Long start, Long count, UserSortType sortBy,
			SortDirectionType sortDirection) {
		Query<User> query = ofy().load().type(User.class);

		if (start != null) {
			query.offset(start.intValue());
		}

		if (count != null) {
			query.limit(count.intValue());
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
	 * com.spacehopperstudios.blogwt.server.services.user.IUserService#getLoginUser
	 * (java.lang.String, java.lang.String) */
	@Override
	public User getLoginUser (String username, String password) {
		User user = ofy().load().type(User.class).filter("username", username)
				.first().now();

		if (user != null
				&& !StringUtils.sha1Hash(password + SALT).equals(user.password)) {
			user = null;
		}

		return addAvatar(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.blogwt.server.services.user.IUserService#
	 * updateUserIdLastLoggedIn(java.lang.Long) */
	@Override
	public void updateUserIdLastLoggedIn (Long userId) {
		User user = getUser(userId);
		user.lastLoggedIn = new Date();
		updateUser(user);
	}

	private User addAvatar (User user) {
		return user == null ? null : user
				.avatar("https://secure.gravatar.com/avatar/"
						+ StringUtils.md5Hash(user.email.trim().toLowerCase()));
	}

}
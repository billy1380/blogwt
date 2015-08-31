//  
//  UserService.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Role;
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
	 * @see com.willshex.blogwt.server.services.user.IUserService#addUser
	 * (com.willshex.blogwt.shared.api.datatypes.User) */
	@Override
	public User addUser (User user) {
		if (user.created == null) {
			user.added = user.created = new Date();
		}

		user.password = generatePassword(user.password);

		if (user.roles != null) {
			user.roleKeys = new ArrayList<Key<Role>>();

			for (Role role : user.roles) {
				user.roleKeys.add(Key.create(role));
			}
		}

		if (user.permissions != null) {
			user.permissionKeys = new ArrayList<Key<Permission>>();
			for (Permission permission : user.permissions) {
				user.permissionKeys.add(Key.create(permission));
			}
		}

		Key<User> key = ofy().save().entity(user).now();
		user.id = Long.valueOf(key.getId());

		return user;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#updateUser
	 * (com.willshex.blogwt.shared.api.datatypes.User) */
	@Override
	public User updateUser (User user) {
		ofy().save().entity(user).now();
		return addAvatar(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#deleteUser
	 * (com.willshex.blogwt.shared.api.datatypes.User) */
	@Override
	public void deleteUser (User user) {
		ofy().delete().entity(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#getUsers
	 * (java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatypes.UserSortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<User> getUsers (Integer start, Integer count,
			UserSortType sortBy, SortDirectionType sortDirection) {
		Query<User> query = ofy().load().type(User.class);

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

		return addAvatars(query.list());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#getLoginUser
	 * (java.lang.String, java.lang.String) */
	@Override
	public User getLoginUser (String username, String password) {
		User user = ofy().load().type(User.class).filter("username", username)
				.first().now();

		if (!verifyPassword(user, password)) {
			user = null;
		}

		return addAvatar(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#
	 * updateUserIdLastLoggedIn(java.lang.Long) */
	@Override
	public void updateUserIdLastLoggedIn (Long userId) {
		updateUser(getUser(userId).lastLoggedIn(new Date()));
	}

	private List<User> addAvatars (List<User> users) {
		if (users != null) {
			for (User user : users) {
				if (user.avatar == null) {
					addAvatar(user);
				}
			}
		}
		return users;
	}

	private User addAvatar (User user) {
		return user == null ? null : user.avatar(UserHelper
				.emailAvatar(user.email));
	}

	@Override
	public void addUserBatch (Collection<User> users) {
		for (User user : users) {
			if (user.created == null) {
				user.added = user.created = new Date();
			}

			if (user.roles != null) {
				user.roleKeys = new ArrayList<Key<Role>>();

				for (Role role : user.roles) {
					user.roleKeys.add(Key.create(role));
				}
			}

			if (user.permissions != null) {
				user.permissionKeys = new ArrayList<Key<Permission>>();
				for (Permission permission : user.permissions) {
					user.permissionKeys.add(Key.create(permission));
				}
			}

			user.password = generatePassword(user.password);
		}

		ofy().save().entities(users).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#getUsernameUser
	 * (java.lang.String) */
	@Override
	public User getUsernameUser (String username) {
		return addAvatar(ofy().load().type(User.class)
				.filter("username", username).first().now());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#verifyPassword(
	 * com.willshex.blogwt.shared.api.datatype.User, java.lang.String) */
	@Override
	public Boolean verifyPassword (User user, String password) {
		return Boolean.valueOf(user != null
				&& StringUtils.sha1Hash(password + SALT).equals(user.password));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#generatePassword
	 * (java.lang.String) */
	@Override
	public String generatePassword (String password) {
		return StringUtils.sha1Hash(password + SALT);
	}

}
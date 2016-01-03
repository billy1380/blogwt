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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.server.helper.EmailHelper;
import com.willshex.blogwt.server.helper.InflatorHelper;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.helper.ServletHelper;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.service.ContextAwareServlet;

final class UserService implements IUserService {

	private static final String SALT = "af1d3250-f8d1-11e4-bbd2-7054d251af02";
	private static final String ACTION_EMAIL_TEMPLATE = "Hi ${user.forename},\n\nPlease click the link below to ${action}:\n\n${link}\n\n${property.value}";

	public String getName () {
		return NAME;
	}

	public User getUser (Long id) {
		return addAvatar(
				ofy().load().type(User.class).id(id.longValue()).now());
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

		SearchHelper.queueToIndex(getName(), user.id);

		return user;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#updateUser
	 * (com.willshex.blogwt.shared.api.datatypes.User) */
	@Override
	public User updateUser (User user) {
		ofy().save().entity(user).now();

		SearchHelper.queueToIndex(getName(), user.id);

		return addAvatar(user);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.user.IUserService#deleteUser
	 * (com.willshex.blogwt.shared.api.datatypes.User) */
	@Override
	public void deleteUser (User user) {
		ofy().delete().entity(user);

		SearchHelper.deleteSearch(getName() + user.id.toString());
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
		return user == null ? null
				: user.avatar(UserHelper.emailAvatar(user.email));
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
	 * @see com.willshex.blogwt.server.service.user.IUserService#getUsernameUser
	 * (java.lang.String) */
	@Override
	public User getUsernameUser (String username) {
		return addAvatar(ofy().load().type(User.class)
				.filter("username", username).first().now());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.user.IUserService#verifyPassword(
	 * com.willshex.blogwt.shared.api.datatype.User, java.lang.String) */
	@Override
	public Boolean verifyPassword (User user, String password) {
		return Boolean.valueOf(user != null && StringUtils
				.sha1Hash(password + getSalt()).equals(user.password));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#generatePassword
	 * (java.lang.String) */
	@Override
	public String generatePassword (String password) {
		return StringUtils.sha1Hash(password + getSalt());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.user.IUserService#
	 * addUserRolesAndPermissions(com.willshex.blogwt.shared.api.datatype.User,
	 * java.util.List, java.util.List) */
	@Override
	public User addUserRolesAndPermissions (final User user,
			final List<Role> roles, final List<Permission> permissions) {
		return ofy().transact(new Work<User>() {

			@Override
			public User run () {
				User latest = getUser(user.id);

				Set<Long> current = null;

				if (latest.permissionKeys != null) {
					current = new HashSet<Long>();

					for (Key<Permission> key : latest.permissionKeys) {
						current.add(Long.valueOf(key.getId()));
					}
				}

				if (permissions != null) {
					if (current == null) {
						current = new HashSet<Long>();
					}

					for (Permission permission : permissions) {
						current.add(permission.id);
					}

					latest.permissionKeys = PersistenceService
							.idsToKeys(Permission.class, current);
				}

				if (latest.roleKeys != null) {
					if (current != null) {
						current.clear();
					} else {
						current = new HashSet<Long>();
					}
				}

				if (roles != null) {
					if (current == null) {
						current = new HashSet<Long>();
					}

					for (Role role : roles) {
						current.add(role.id);
					}

					latest.roleKeys = PersistenceService.idsToKeys(Role.class,
							current);
				}

				ofy().save().entity(latest).now();

				return latest;
			}
		});
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.user.IUserService#
	 * removeUserRolesAndPermissions
	 * (com.willshex.blogwt.shared.api.datatype.User, java.util.List,
	 * java.util.List) */
	@Override
	public User removeUserRolesAndPermissions (final User user,
			final List<Role> roles, final List<Permission> permissions) {
		return ofy().transact(new Work<User>() {

			@Override
			public User run () {
				User latest = getUser(user.id);

				Set<Long> current = null;

				if (latest.permissionKeys != null) {
					current = new HashSet<Long>();

					for (Key<Permission> key : latest.permissionKeys) {
						current.add(Long.valueOf(key.getId()));
					}

					if (permissions != null) {
						for (Permission permission : permissions) {
							current.remove(permission.id);
						}
					}

					latest.permissionKeys = PersistenceService
							.idsToKeys(Permission.class, current);
				}

				if (latest.roleKeys != null) {
					if (current != null) {
						current.clear();
					} else {
						current = new HashSet<Long>();
					}

					for (Key<Role> key : latest.roleKeys) {
						current.add(Long.valueOf(key.getId()));
					}

					if (roles != null) {
						for (Role role : roles) {
							current.remove(role.id);
						}
					}

					latest.roleKeys = PersistenceService.idsToKeys(Role.class,
							current);
				}

				ofy().save().entity(latest).now();

				return latest;
			}
		});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#resetPassword(com
	 * .willshex.blogwt.shared.api.datatype.User) */
	@Override
	public void resetPassword (User user) {
		sendActionEmail(user, "changepassword/reset", "reset your password");
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#verifyAccount(com
	 * .willshex.blogwt.shared.api.datatype.User) */
	@Override
	public void verifyAccount (User user) {
		sendActionEmail(user, "verifyaccount", "verify your account");
	}

	/**
	 * @param user
	 */
	private void sendActionEmail (User user, String action, String actionName) {
		if (user.forename == null) {
			user = getUser(user.id);
		}

		user.actionCode = UUID.randomUUID().toString();
		user = updateUser(user);

		Map<String, Object> values = new HashMap<String, Object>();

		String url = ServletHelper
				.constructBaseUrl(ContextAwareServlet.REQUEST.get());

		values.put("user", user);
		values.put("link",
				String.format("%s#%s/%s", url, action, user.actionCode));
		values.put("action", actionName);
		values.put("property", PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.TITLE));

		EmailHelper.sendEmail(user.email, UserHelper.name(user), actionName,
				InflatorHelper.inflate(values, ACTION_EMAIL_TEMPLATE), false);

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#getActionCodeUser
	 * (java.lang.String) */
	@Override
	public User getActionCodeUser (String actionCode) {
		return addAvatar(ofy().load().type(User.class)
				.filter("actionCode", actionCode).first().now());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#getEmailUser(java
	 * .lang.String) */
	@Override
	public User getEmailUser (String email) {
		return addAvatar(ofy().load().type(User.class).filter("email", email)
				.first().now());
	}

	private String getSalt () {
		String salt = SALT;
		Property hashSaltProperty;
		if ((hashSaltProperty = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.PASSWORD_HASH_SALT)) != null) {
			salt = hashSaltProperty.value;
		}

		return salt;
	}

	/**
	 * @param user
	 * @return
	 */
	private Document toDocument (User user) {
		Document document = null;

		if (user != null) {
			Document.Builder documentBuilder = Document.newBuilder();
			documentBuilder.setId(getName() + user.id.toString())
					.addField(Field.newBuilder().setName("username")
							.setAtom(user.username))
					.addField(Field.newBuilder().setName("name")
							.setText(UserHelper.name(user)))
					.addField(Field.newBuilder().setName("forename")
							.setText(user.forename))
					.addField(Field.newBuilder().setName("surname")
							.setText(user.surname))
					.addField(Field.newBuilder().setName("email")
							.setText(user.email))
					.addField(Field.newBuilder().setName("created")
							.setDate(user.created))
					.addField(Field.newBuilder().setName("group")
							.setText(user.group))
					.addField(Field.newBuilder().setName("summary")
							.setText(user.summary));

			if (user.roles != null) {
				for (Role role : user.roles) {
					documentBuilder.addField(Field.newBuilder().setName("role")
							.setText(role.name));
				}
			}

			if (user.permissions != null) {
				for (Permission permission : user.permissions) {
					documentBuilder.addField(Field.newBuilder()
							.setName("permission").setText(permission.name));
				}
			}

			document = documentBuilder.build();
		}

		return document;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.user.IUserService#indexAll() */
	@Override
	public void indexAll () {
		Pager pager = PagerHelper.createDefaultPager();

		List<User> users = null;
		do {
			users = getUsers(pager.start, pager.count, null, null);

			for (User user : users) {
				SearchHelper.queueToIndex(getName(), user.id);
			}

			PagerHelper.moveForward(pager);
		} while (users != null && users.size() >= pager.count.intValue());
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.server.service.user.IUserService#indexUser(java.lang.
	 * Long) */
	@Override
	public void indexUser (Long id) {
		User user = getUser(id);

		if (user.roleKeys != null) {
			user.roles = RoleServiceProvider.provide().getIdRolesBatch(
					PersistenceService.keysToIds(user.roleKeys));
		}

		if (user.permissionKeys != null) {
			user.permissions = PermissionServiceProvider.provide()
					.getIdPermissionsBatch(
							PersistenceService.keysToIds(user.permissionKeys));
		}

		SearchHelper.indexDocument(toDocument(user));
	}

}
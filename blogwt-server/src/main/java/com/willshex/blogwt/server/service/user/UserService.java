//  
//  UserService.java
//  blogwt
//
//  Created by William Shakour on 12 May 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.user;

import static com.willshex.blogwt.server.helper.PersistenceHelper.id;
import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

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
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.EmailHelper;
import com.willshex.blogwt.server.helper.InflatorHelper;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.helper.SearchHelper;
import com.willshex.blogwt.server.helper.ServletHelper;
import com.willshex.blogwt.server.helper.UserHelper;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.UserSortType;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.server.ContextAwareServlet;
import com.willshex.utility.StringUtils;

final class UserService implements IUserService {

	private static final String SALT = "af1d3250-f8d1-11e4-bbd2-7054d251af02";
	private static final String ACTION_EMAIL_TEMPLATE = "Hi ${user.forename},\n\nPlease click the link below to ${action}:\n\n${link}\n\n${property.value}";

	public String getName() {
		return NAME;
	}

	public User getUser(Long id) {
		return addAvatar(id(load(), id));
	}

	private LoadType<User> load() {
		return provide().load().type(User.class);
	}
	@Override
	public User addUser(User user) {
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

		Key<User> key = provide().save().entity(user).now();
		user.id = keyToId(key);

		SearchHelper.queueToIndex(getName(), user.id);

		return user;
	}
	@Override
	public User updateUser(User user) {
		provide().save().entity(user).now();

		SearchHelper.queueToIndex(getName(), user.id);

		return addAvatar(user);
	}
	@Override
	public void deleteUser(User user) {
		provide().delete().entity(user);

		SearchHelper.deleteSearch(this, getName() + user.id.toString());
	}
	@Override
	public List<User> getUsers(Integer start, Integer count,
			UserSortType sortBy, SortDirectionType sortDirection) {
		return addAvatars(PersistenceHelper.pagedAndSorted(load(), start, count,
				sortBy, this, sortDirection));
	}
	@Override
	public User getLoginUser(String username, String password) {
		User user = PersistenceHelper.one(load()
				.filter(map(UserSortType.UserSortTypeUsername), username));

		if (mismatch(user, password)) {
			user = null;
		}

		return addAvatar(user);
	}
	@Override
	public void updateUserIdLastLoggedIn(Long userId) {
		updateUser(getUser(userId).lastLoggedIn(new Date()));
	}

	private List<User> addAvatars(List<User> users) {
		if (users != null) {
			for (User user : users) {
				if (user.avatar == null) {
					addAvatar(user);
				}
			}
		}
		return users;
	}

	private User addAvatar(User user) {
		if (user != null && user.email != null) {
			user.avatar = UserHelper.emailGravatar(user.email);
		}

		return user;
	}

	@Override
	public void addUserBatch(Iterable<User> users) {
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

		provide().save().entities(users).now();
	}
	@Override
	public User getUsernameUser(String username) {
		return addAvatar(PersistenceHelper.one(load()
				.filter(map(UserSortType.UserSortTypeUsername), username)));
	}
	@Override
	public Boolean verifyPassword(User user, String password) {
		return Boolean.valueOf(user != null
				&& generatePassword(password).equals(user.password));
	}

	private boolean mismatch(User user, String password) {
		return !Boolean.TRUE.equals(verifyPassword(user, password));
	}
	@Override
	public String generatePassword(String password) {
		return StringUtils.sha1Hash(password + getSalt());
	}
	@Override
	public User addUserRolesAndPermissions(final User user,
			final List<Role> roles, final List<Permission> permissions) {
		return provide().transact(new Work<User>() {

			@Override
			public User run() {
				User latest = getUser(user.id);

				Set<Long> current = null;

				if (latest.permissionKeys != null) {
					current = new HashSet<Long>();

					for (Key<Permission> key : latest.permissionKeys) {
						current.add(keyToId(key));
					}
				}

				if (permissions != null) {
					if (current == null) {
						current = new HashSet<Long>();
					}

					for (Permission permission : permissions) {
						current.add(permission.id);
					}

					latest.permissionKeys = PersistenceHelper
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

					latest.roleKeys = PersistenceHelper.idsToKeys(Role.class,
							current);
				}

				provide().save().entity(latest).now();

				return latest;
			}
		});
	}
	@Override
	public User removeUserRolesAndPermissions(final User user,
			final List<Role> roles, final List<Permission> permissions) {
		return provide().transact(new Work<User>() {

			@Override
			public User run() {
				User latest = getUser(user.id);

				Set<Long> current = null;

				if (latest.permissionKeys != null) {
					current = new HashSet<Long>();

					for (Key<Permission> key : latest.permissionKeys) {
						current.add(keyToId(key));
					}

					if (permissions != null) {
						for (Permission permission : permissions) {
							current.remove(permission.id);
						}
					}

					latest.permissionKeys = PersistenceHelper
							.idsToKeys(Permission.class, current);
				}

				if (latest.roleKeys != null) {
					if (current != null) {
						current.clear();
					} else {
						current = new HashSet<Long>();
					}

					for (Key<Role> key : latest.roleKeys) {
						current.add(keyToId(key));
					}

					if (roles != null) {
						for (Role role : roles) {
							current.remove(role.id);
						}
					}

					latest.roleKeys = PersistenceHelper.idsToKeys(Role.class,
							current);
				}

				provide().save().entity(latest).now();

				return latest;
			}
		});
	}
	@Override
	public void resetPassword(User user) {
		sendActionEmail(user, "changepassword/reset", "reset your password");
	}
	@Override
	public void verifyAccount(User user) {
		sendActionEmail(user, "verifyaccount", "verify your account");
	}

	/**
	 * @param user
	 */
	private void sendActionEmail(User user, String action, String actionName) {
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
				String.format("%s/#%s/%s", url, action, user.actionCode));
		values.put("action", actionName);
		values.put("property", PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.TITLE));

		EmailHelper.sendEmail(user.email, UserHelper.name(user), actionName,
				InflatorHelper.inflate(values, ACTION_EMAIL_TEMPLATE), false);

	}
	@Override
	public User getActionCodeUser(String actionCode) {
		return addAvatar(PersistenceHelper.one(load()
				.filter(map(UserSortType.UserSortTypeActionCode), actionCode)));
	}
	@Override
	public User getEmailUser(String email) {
		return addAvatar(PersistenceHelper.one(
				load().filter(map(UserSortType.UserSortTypeEmail), email)));
	}

	private String getSalt() {
		String salt = SALT;
		Property hashSaltProperty;
		if ((hashSaltProperty = PropertyServiceProvider.provide()
				.getNamedProperty(PropertyHelper.PASSWORD_HASH_SALT)) != null) {
			salt = hashSaltProperty.value;
		}

		return salt;
	}
	@Override
	public Document toDocument(User user) {
		Document document = null;

		if (user != null) {
			if (user.roleKeys != null) {
				user.roles = PersistenceHelper.batchLookupKeys(
						RoleServiceProvider.provide(), user.roleKeys);
			}

			if (user.permissionKeys != null) {
				user.permissions = PersistenceHelper.batchLookupKeys(
						PermissionServiceProvider.provide(),
						user.permissionKeys);
			}

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
	@Override
	public void indexAll() {
		SearchHelper.indexAll(getName(), this::getUsers);
	}
	@Override
	public List<User> getIdUserBatch(Iterable<Long> ids) {
		return addAvatars(new ArrayList<User>(load().ids(ids).values()));
	}
	@Override
	public void index(Long id) {
		SearchHelper.indexDocument(this, getName(), toDocument(getUser(id)));
	}
	@Override
	public List<User> search(String query, Integer start, Integer count,
			String sortBy, SortDirectionType direction) {
		Results<ScoredDocument> matches = SearchHelper.getIndex(this)
				.search(query);
		List<User> users = new ArrayList<User>();
		String id;
		User user;
		int limit = count;
		final String userServiceName = getName();
		for (ScoredDocument scoredDocument : matches) {
			if (limit == 0) {
				break;
			}

			if ((id = scoredDocument.getId()).startsWith(userServiceName)) {
				user = getUser(Long.valueOf(id.replace(userServiceName, "")));
				if (user != null) {
					users.add(user);
				}
			}

			limit--;
		}

		return users;
	}
	@Override
	public User getEmailLoginUser(String email, String password) {
		User user = getEmailUser(email);

		if (mismatch(user, password)) {
			user = null;
		}

		return user;
	}
	@Override
	public String search(Collection<User> resultHolder, String query,
			String next, Integer count, String sortBy,
			SortDirectionType direction) {
		throw new UnsupportedOperationException();
	}
	@Override
	public List<User> get(Iterable<Long> ids) {
		return getIdUserBatch(ids);
	}

}

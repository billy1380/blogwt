package com.willshex.blogwt.server.tool;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarHeader;
import org.kamranzafar.jtar.TarOutputStream;

import com.jcraft.jzlib.GZIPOutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.datatype.Notification;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostContent;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.PushToken;
import com.willshex.blogwt.shared.api.datatype.Rating;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.datatype.User;

public class Exporter {

	private OutputStream outputStream;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public Exporter (OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void exportAll () throws IOException {
		try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
				TarOutputStream tar = new TarOutputStream(gzip)) {
			
			export(User.class, this::hydrateUsers, tar);
			export(Session.class, this::hydrateSessions, tar);
			export(Post.class, this::hydratePosts, tar);
			export(Resource.class, null, tar);
			export(Permission.class, null, tar);
			export(Role.class, this::hydrateRoles, tar);
			export(Property.class, null, tar);
			export(PostContent.class, null, tar);
			export(Page.class, this::hydratePages, tar);
			export(Tag.class, null, tar);
			export(ArchiveEntry.class, null, tar);
			export(Notification.class, this::hydrateNotifications, tar);
			export(MetaNotification.class, null, tar);
			export(PushToken.class, null, tar);
			export(NotificationSetting.class, null, tar);
			export(Relationship.class, this::hydrateRelationships, tar);
			export(Rating.class, null, tar);
			export(Rating.class, null, tar);
			export(GeneratedDownload.class, null, tar);
		}
	}

	private <T extends DataType> void export (Class<T> type,
			Consumer<List<T>> hydrator, TarOutputStream tar)
			throws IOException {
		List<T> entities = provide().load().type(type).list();

		if (hydrator != null) {
			hydrator.accept(entities);
		}

		String typeName = type.getSimpleName();
		long maxId = 0;
		for (T entity : entities) {
			if (entity.id != null && entity.id.longValue() > maxId) {
				maxId = entity.id.longValue();
			}

			byte[] data = gson.toJson(entity.toJson())
					.getBytes(StandardCharsets.UTF_8);
			String name = typeName + "/" + entity.id.toString() + ".json";

			TarEntry entry = new TarEntry(TarHeader.createHeader(name, data.length, System.currentTimeMillis() / 1000, false, 0644));
			tar.putNextEntry(entry);
			tar.write(data);
		}

		byte[] autoincData = Long.toString(maxId)
				.getBytes(StandardCharsets.UTF_8);
		String autoincName = typeName + "/_.autoinc";
		TarEntry autoincEntry = new TarEntry(TarHeader.createHeader(autoincName, autoincData.length, System.currentTimeMillis() / 1000, false, 0644));
		tar.putNextEntry(autoincEntry);
		tar.write(autoincData);
	}

	private void hydratePosts (List<Post> posts) {
		List<Long> contentIds = new ArrayList<>();
		for (Post post : posts) {
			if (post.contentKey != null) {
				contentIds.add(keyToId(post.contentKey));
			}

			if (post.authorKey != null) {
				post.author = userFromKey(post.authorKey);
			}
		}

		Map<Long, PostContent> contents = provide().load().type(PostContent.class)
				.ids(contentIds);

		for (Post post : posts) {
			if (post.contentKey != null) {
				post.content = contents.get(keyToId(post.contentKey));
			}
		}
	}

	private void hydratePages (List<Page> pages) {
		for (Page page : pages) {
			if (page.postKeys != null) {
				page.posts = new ArrayList<>();
				for (Key<Post> key : page.postKeys) {
					page.posts.add(postFromKey(key));
				}
			}

			if (page.parentKey != null) {
				page.parent = pageFromKey(page.parentKey);
			}

			if (page.ownerKey != null) {
				page.owner = userFromKey(page.ownerKey);
			}
		}
	}

	private void hydrateUsers (List<User> users) {
		for (User user : users) {
			if (user.roleKeys != null) {
				user.roles = new ArrayList<>();
				for (Key<Role> key : user.roleKeys) {
					user.roles.add(roleFromKey(key));
				}
			}

			if (user.permissionKeys != null) {
				user.permissions = new ArrayList<>();
				for (Key<Permission> key : user.permissionKeys) {
					user.permissions.add(permissionFromKey(key));
				}
			}
		}
	}

	private void hydrateRoles (List<Role> roles) {
		for (Role role : roles) {
			if (role.permissionKeys != null) {
				role.permissions = new ArrayList<>();
				for (Key<Permission> key : role.permissionKeys) {
					role.permissions.add(permissionFromKey(key));
				}
			}
		}
	}

	private void hydrateSessions (List<Session> sessions) {
		for (Session session : sessions) {
			if (session.userKey != null) {
				session.user = userFromKey(session.userKey);
			}
		}
	}

	private void hydrateNotifications (List<Notification> notifications) {
		for (Notification notification : notifications) {
			if (notification.senderKey != null) {
				notification.sender = userFromKey(notification.senderKey);
			}

			if (notification.targetKey != null) {
				notification.target = userFromKey(notification.targetKey);
			}
		}
	}

	private void hydrateRelationships (List<Relationship> relationships) {
		for (Relationship relationship : relationships) {
			if (relationship.oneKey != null) {
				relationship.one = userFromKey(relationship.oneKey);
			}

			if (relationship.anotherKey != null) {
				relationship.another = userFromKey(relationship.anotherKey);
			}
		}
	}

	private Role roleFromKey (Key<Role> key) {
		if (key == null) return null;
		Role instance = new Role();
		instance.id = keyToId(key);
		return instance;
	}

	private Permission permissionFromKey (Key<Permission> key) {
		if (key == null) return null;
		Permission instance = new Permission();
		instance.id = keyToId(key);
		return instance;
	}

	private User userFromKey (Key<User> key) {
		if (key == null) return null;
		User instance = new User();
		instance.id = keyToId(key);
		return instance;
	}

	private Post postFromKey (Key<Post> key) {
		if (key == null) return null;
		Post instance = new Post();
		instance.id = keyToId(key);
		return instance;
	}

	private Page pageFromKey (Key<Page> key) {
		if (key == null) return null;
		Page instance = new Page();
		instance.id = keyToId(key);
		return instance;
	}

}

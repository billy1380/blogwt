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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Key;
import com.jcraft.jzlib.GZIPOutputStream;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Comment;
import com.willshex.blogwt.shared.api.datatype.DataType;
import com.willshex.blogwt.shared.api.datatype.Field;
import com.willshex.blogwt.shared.api.datatype.Form;
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
import com.willshex.blogwt.shared.api.datatype.Reaction;
import com.willshex.blogwt.shared.api.datatype.Relationship;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.datatype.Role;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.datatype.User;

public class Exporter {

	private OutputStream outputStream;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public Exporter(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void exportAll() throws IOException {
		try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
				TarOutputStream tar = new TarOutputStream(gzip)) {
			export(ArchiveEntry.class, this::convertKeysToIdsArchiveEntry, tar);
			export(Comment.class, this::convertKeysToIdsComments, tar);
			export(Field.class, null, tar);
			export(Form.class, null, tar);
			export(GeneratedDownload.class, this::convertKeysToIdsGeneratedDownloads, tar);
			export(MetaNotification.class, null, tar);
			export(Notification.class, this::convertKeysToIdsNotifications, tar);
			export(NotificationSetting.class, this::convertKeysToIdsNotificationSettings, tar);
			export(Page.class, this::convertKeysToIdsPages, tar);
			export(Permission.class, null, tar);
			export(Post.class, this::convertKeysToIdsPosts, tar);
			export(PostContent.class, this::convertKeysToIdsPostContents, tar);
			export(Property.class, null, tar);
			export(PushToken.class, this::convertKeysToIdsPushTokens, tar);
			export(Rating.class, this::convertKeysToIdsRatings, tar);
			export(Reaction.class, this::convertKeysToIdsReactions, tar);
			export(Relationship.class, this::convertKeysToIdsRelationships, tar);
			export(Resource.class, null, tar);
			export(Role.class, this::convertKeysToIdsRoles, tar);
			export(Session.class, this::convertKeysToIdsSessions, tar);
			export(Tag.class, this::convertKeysToIdsTags, tar);
			export(User.class, this::convertKeysToIdsUsers, tar);
		}
	}

	private <T extends DataType> void export(Class<T> type,
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

			TarEntry entry = new TarEntry(
					TarHeader.createHeader(name, data.length, System.currentTimeMillis() / 1000, false, 0644));
			tar.putNextEntry(entry);
			tar.write(data);
		}

		byte[] autoincData = Long.toString(maxId)
				.getBytes(StandardCharsets.UTF_8);
		String autoincName = typeName + "/_.autoinc";
		TarEntry autoincEntry = new TarEntry(TarHeader.createHeader(autoincName, autoincData.length,
				System.currentTimeMillis() / 1000, false, 0644));
		tar.putNextEntry(autoincEntry);
		tar.write(autoincData);
	}

	private void convertKeysToIdsPosts(List<Post> posts) {
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

	private void convertKeysToIdsPostContents(List<PostContent> postContents) {
		for (PostContent postContent : postContents) {
			if (postContent.postKey != null) {
				postContent.post = postFromKey(postContent.postKey);
			}
		}
	}

	private void convertKeysToIdsPages(List<Page> pages) {
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

	private void convertKeysToIdsUsers(List<User> users) {
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

	private void convertKeysToIdsRoles(List<Role> roles) {
		for (Role role : roles) {
			if (role.permissionKeys != null) {
				role.permissions = new ArrayList<>();
				for (Key<Permission> key : role.permissionKeys) {
					role.permissions.add(permissionFromKey(key));
				}
			}
		}
	}

	private void convertKeysToIdsSessions(List<Session> sessions) {
		for (Session session : sessions) {
			if (session.userKey != null) {
				session.user = userFromKey(session.userKey);
			}
		}
	}

	private void convertKeysToIdsTags(List<Tag> tags) {
		for (Tag tag : tags) {
			if (tag.postKeys != null) {
				tag.posts = new ArrayList<>();
				for (Key<Post> key : tag.postKeys) {
					tag.posts.add(postFromKey(key));
				}
			}
		}
	}

	private void convertKeysToIdsNotifications(List<Notification> notifications) {
		for (Notification notification : notifications) {
			if (notification.metaKey != null) {
				notification.meta = metaNotificationFromKey(notification.metaKey);
			}

			if (notification.senderKey != null) {
				notification.sender = userFromKey(notification.senderKey);
			}

			if (notification.targetKey != null) {
				notification.target = userFromKey(notification.targetKey);
			}
		}
	}

	private void convertKeysToIdsRelationships(List<Relationship> relationships) {
		for (Relationship relationship : relationships) {
			if (relationship.oneKey != null) {
				relationship.one = userFromKey(relationship.oneKey);
			}

			if (relationship.anotherKey != null) {
				relationship.another = userFromKey(relationship.anotherKey);
			}
		}
	}

	private void convertKeysToIdsArchiveEntry(List<ArchiveEntry> archiveEntries) {
		for (ArchiveEntry archiveEntry : archiveEntries) {
			if (archiveEntry.postKeys != null) {
				archiveEntry.posts = new ArrayList<>();
				for (Key<Post> key : archiveEntry.postKeys) {
					archiveEntry.posts.add(postFromKey(key));
				}
			}
		}
	}

	private void convertKeysToIdsComments(List<Comment> comments) {
		for (Comment comment : comments) {
			if (comment.ownerKey != null) {
				comment.owner = userFromKey(comment.ownerKey);
			}

			if (comment.reactionKeys != null) {
				comment.reactions = new ArrayList<>();
				for (Key<Reaction> key : comment.reactionKeys) {
					comment.reactions.add(reactionFromKey(key));
				}
			}

			if (comment.parentKey != null) {
				comment.parent = dataTypeFromKey(comment.parentKey);
			}

			if (comment.replyKeys != null) {
				comment.replies = new ArrayList<>();
				for (Key<Comment> key : comment.replyKeys) {
					comment.replies.add(commentFromKey(key));
				}
			}
		}
	}

	private void convertKeysToIdsGeneratedDownloads(List<GeneratedDownload> generatedDownloads) {
		for (GeneratedDownload generatedDownload : generatedDownloads) {
			if (generatedDownload.userKey != null) {
				generatedDownload.user = userFromKey(generatedDownload.userKey);
			}
		}
	}

	private void convertKeysToIdsNotificationSettings(List<NotificationSetting> notificationSettings) {
		for (NotificationSetting notificationSetting : notificationSettings) {
			if (notificationSetting.userKey != null) {
				notificationSetting.user = userFromKey(notificationSetting.userKey);
			}

			if (notificationSetting.metaKey != null) {
				notificationSetting.meta = metaNotificationFromKey(notificationSetting.metaKey);
			}
		}
	}

	private void convertKeysToIdsPushTokens(List<PushToken> pushTokens) {
		for (PushToken pushToken : pushTokens) {
			if (pushToken.userKey != null) {
				pushToken.user = userFromKey(pushToken.userKey);
			}
		}
	}

	private void convertKeysToIdsRatings(List<Rating> ratings) {
		for (Rating rating : ratings) {
			if (rating.byKey != null) {
				rating.by = userFromKey(rating.byKey);
			}

		}
	}

	private void convertKeysToIdsReactions(List<Reaction> reactions) {
		for (Reaction reaction : reactions) {
			if (reaction.ownerKey != null) {
				reaction.owner = userFromKey(reaction.ownerKey);
			}

			if (reaction.parentKey != null) {
				reaction.parent = dataTypeFromKey(reaction.parentKey);
			}

		}
	}

	private MetaNotification metaNotificationFromKey(Key<MetaNotification> key) {
		if (key == null)
			return null;
		MetaNotification instance = new MetaNotification();
		instance.id = keyToId(key);
		return instance;
	}

	private Reaction reactionFromKey(Key<Reaction> key) {
		if (key == null)
			return null;
		Reaction instance = new Reaction();
		instance.id = keyToId(key);
		return instance;
	}

	private DataType dataTypeFromKey(Key<DataType> key) {
		if (key == null)
			return null;
		DataType instance = new DataType();
		instance.id = keyToId(key);
		return instance;
	}

	private Comment commentFromKey(Key<Comment> key) {
		if (key == null)
			return null;
		Comment instance = new Comment();
		instance.id = keyToId(key);
		return instance;
	}

	private Role roleFromKey(Key<Role> key) {
		if (key == null)
			return null;
		Role instance = new Role();
		instance.id = keyToId(key);
		return instance;
	}

	private Permission permissionFromKey(Key<Permission> key) {
		if (key == null)
			return null;
		Permission instance = new Permission();
		instance.id = keyToId(key);
		return instance;
	}

	private User userFromKey(Key<User> key) {
		if (key == null)
			return null;
		User instance = new User();
		instance.id = keyToId(key);
		return instance;
	}

	private Post postFromKey(Key<Post> key) {
		if (key == null)
			return null;
		Post instance = new Post();
		instance.id = keyToId(key);
		return instance;
	}

	private Page pageFromKey(Key<Page> key) {
		if (key == null)
			return null;
		Page instance = new Page();
		instance.id = keyToId(key);
		return instance;
	}

}

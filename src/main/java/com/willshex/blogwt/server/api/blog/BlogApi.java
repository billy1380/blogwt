//  
//  BlogApi.java
//  blogwt
//
//  Created by William Shakour on May 12, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Key;
import com.willshex.blogwt.server.api.exception.AuthorisationException;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.ArchiveEntryValidator;
import com.willshex.blogwt.server.api.validation.PostValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.ResourceValidator;
import com.willshex.blogwt.server.api.validation.RoleValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.server.service.role.RoleServiceProvider;
import com.willshex.blogwt.server.service.tag.TagServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.blog.call.CreatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.CreatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.DeletePostRequest;
import com.willshex.blogwt.shared.api.blog.call.DeletePostResponse;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostResponse;
import com.willshex.blogwt.shared.api.blog.call.GetPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetRelatedPostsResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.api.blog.call.GetTagsRequest;
import com.willshex.blogwt.shared.api.blog.call.GetTagsResponse;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePostResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdateResourceResponse;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Post;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.Tag;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.RoleHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.server.InputValidationException;
import com.willshex.gson.json.service.server.ServiceException;
import com.willshex.gson.json.service.shared.StatusType;

public final class BlogApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(BlogApi.class.getName());

	public UpdateResourceResponse updateResource (UpdateResourceRequest input) {
		LOG.finer("Entering updateResource");
		UpdateResourceResponse output = new UpdateResourceResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updateResource");
		return output;
	}

	public GetResourceResponse getResource (GetResourceRequest input) {
		LOG.finer("Entering getResource");
		GetResourceResponse output = new GetResourceResponse();
		try {
			ApiValidator.notNull(input, GetResourceRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			output.resource = input.resource = ResourceValidator.lookup(
					input.resource, "input.resource");

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getResource");
		return output;
	}

	public GetArchiveEntriesResponse getArchiveEntries (
			GetArchiveEntriesRequest input) {
		LOG.finer("Entering getArchiveEntries");
		GetArchiveEntriesResponse output = new GetArchiveEntriesResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getArchiveEntries");
		return output;
	}

	public DeleteResourceResponse deleteResource (DeleteResourceRequest input) {
		LOG.finer("Entering deleteResource");
		DeleteResourceResponse output = new DeleteResourceResponse();
		try {
			ApiValidator.notNull(input, DeleteResourceRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.resource = ResourceValidator.lookup(input.resource,
					"input.resource");

			BlobstoreServiceFactory.getBlobstoreService().delete(
					new BlobKey(input.resource.data.substring(5)));

			ResourceServiceProvider.provide().deleteResource(input.resource);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting deleteResource");
		return output;
	}

	public GetResourcesResponse getResources (GetResourcesRequest input) {
		LOG.finer("Entering getResources");
		GetResourcesResponse output = new GetResourcesResponse();
		try {
			ApiValidator.notNull(input, GetResourcesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			output.resources = ResourceServiceProvider.provide().getResrouces(
					input.pager.start, input.pager.count, null,
					SortDirectionType.SortDirectionTypeDescending);

			output.pager = PagerHelper.moveForward(input.pager);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getResources");
		return output;
	}

	public GetRelatedPostsResponse getRelatedPosts (GetRelatedPostsRequest input) {
		LOG.finer("Entering getRelatedPosts");
		GetRelatedPostsResponse output = new GetRelatedPostsResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getRelatedPosts");
		return output;
	}

	public GetPostResponse getPost (GetPostRequest input) {
		LOG.finer("Entering getPost");
		GetPostResponse output = new GetPostResponse();
		try {
			ApiValidator.notNull(input, GetPostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			output.post = PostValidator.lookup(input.post, "input.post");

			if (output.post != null) {
				output.post.author = UserServiceProvider.provide().getUser(
						Long.valueOf(output.post.authorKey.getId()));
				UserHelper.stripPassword(output.post.author);
			}

			output.post.content = PostServiceProvider.provide().getPostContent(
					output.post);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPost");
		return output;
	}

	public UpdatePostResponse updatePost (UpdatePostRequest input) {
		LOG.finer("Entering updatePost");
		UpdatePostResponse output = new UpdatePostResponse();
		try {
			ApiValidator.notNull(input, UpdatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_POSTS)),
					"input.session.user");

			Post updatedPost = input.post;

			input.post = PostValidator.lookup(input.post, "input.post");
			input.post.content = PostServiceProvider.provide().getPostContent(
					input.post);

			updatedPost = PostValidator.validate(updatedPost, "input.post");

			input.post.commentsEnabled = updatedPost.commentsEnabled;
			input.post.content.body = updatedPost.content.body;
			input.post.listed = updatedPost.listed;
			input.post.summary = updatedPost.summary;

			List<String> removedTags = null;
			if (updatedPost.tags == null) {
				removedTags = input.post.tags;
			} else {
				if (input.post.tags != null) {
					removedTags = new ArrayList<String>();
					for (String tag : input.post.tags) {
						if (!updatedPost.tags.contains(tag)) {
							removedTags.add(tag);
						}
					}
				}
			}

			input.post.tags = updatedPost.tags;
			input.post.title = updatedPost.title;

			// don't change the original publish date
			if (Boolean.TRUE.equals(input.publish)
					&& input.post.published == null) {
				input.post.published = new Date();
			}

			PostServiceProvider.provide().updatePost(input.post, removedTags);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updatePost");
		return output;
	}

	public CreatePostResponse createPost (CreatePostRequest input) {
		LOG.finer("Entering createPost");
		CreatePostResponse output = new CreatePostResponse();
		try {
			ApiValidator.notNull(input, CreatePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_POSTS);
			permissions.add(postPermission);

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, permissions,
					"input.session.user");

			input.post = PostValidator.validate(input.post, "input.post");

			input.post.author = input.session.user;

			if (Boolean.TRUE.equals(input.publish)) {
				input.post.published = new Date();
			}

			input.post.listed = (input.post.listed == null ? Boolean.TRUE
					: input.post.listed);
			input.post.commentsEnabled = (input.post.commentsEnabled == null ? Boolean.FALSE
					: input.post.commentsEnabled);

			output.post = PostServiceProvider.provide().addPost(input.post);

			if (output.post != null) {
				output.post.author = UserServiceProvider.provide().getUser(
						output.post.author.id);
				UserHelper.stripPassword(output.post.author);
			}

			UserHelper.stripPassword(output.post.author);
			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting createPost");
		return output;
	}

	public DeletePostResponse deletePost (DeletePostRequest input) {
		LOG.finer("Entering deletePost");
		DeletePostResponse output = new DeletePostResponse();
		try {
			ApiValidator.notNull(input, DeletePostRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_POSTS);
			permissions.add(postPermission);

			UserValidator.authorisation(input.session.user, permissions,
					"input.session.user");

			input.post = PostValidator.lookup(input.post, "input.post");

			PostServiceProvider.provide().deletePost(input.post);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting deletePost");
		return output;
	}

	public GetPostsResponse getPosts (GetPostsRequest input) {
		LOG.finer("Entering getPosts");
		GetPostsResponse output = new GetPostsResponse();
		try {
			ApiValidator.notNull(input, GetPostsRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			Boolean showAll = Boolean.TRUE.equals(input.showAll) ? Boolean.TRUE
					: Boolean.FALSE;
			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");

					List<Permission> permissions = new ArrayList<Permission>();
					Permission postPermission = PermissionServiceProvider
							.provide().getCodePermission(
									PermissionHelper.MANAGE_POSTS);
					permissions.add(postPermission);

					input.session.user = UserServiceProvider.provide().getUser(
							Long.valueOf(input.session.userKey.getId()));

					try {
						UserValidator.authorisation(input.session.user,
								permissions, "input.session.user");
					} catch (AuthorisationException aEx) {
						showAll = Boolean.FALSE;
					}
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			if (input.includePostContents == null) {
				input.includePostContents = Boolean.FALSE;
			}

			boolean postsForTag = false, postsForArchiveEntry = false, postsForQuery = false;
			if (input.tag != null && input.tag.length() > 0) {
				postsForTag = true;
				Tag tag = TagServiceProvider.provide().getSlugTag(input.tag);

				if (tag != null) {
					output.posts = PostServiceProvider.provide().getPostBatch(
							PersistenceService.keysToIds(tag.postKeys));
				}
			}

			if (!postsForTag && input.archiveEntry != null) {
				postsForTag = true;
				if (input.archiveEntry.posts != null) {
					output.posts = input.archiveEntry.posts = PostValidator
							.lookupAll(input.archiveEntry.posts,
									"input.archiveEntry.posts");
				} else {
					input.archiveEntry = ArchiveEntryValidator.lookup(
							input.archiveEntry, "input.archiveEntry");

					output.posts = PostServiceProvider.provide().getPostBatch(
							PersistenceService
									.keysToIds(input.archiveEntry.postKeys));
				}
			}

			if (!postsForTag && !postsForArchiveEntry && input.query != null) {
				postsForQuery = true;
				if (input.session != null && input.session.user != null) {
					output.posts = PostServiceProvider
							.provide()
							.getUserViewablePartialSlugPosts(
									input.query,
									input.session.user,
									showAll,
									input.includePostContents,
									input.pager.start,
									input.pager.count,
									PostSortType.fromString(input.pager.sortBy),
									input.pager.sortDirection);
				} else {
					output.posts = PostServiceProvider
							.provide()
							.getPartialSlugPosts(
									input.query,
									showAll,
									input.includePostContents,
									input.pager.start,
									input.pager.count,
									PostSortType.PostSortTypePublished,
									SortDirectionType.SortDirectionTypeDescending);
				}
			}

			if (!postsForTag && !postsForArchiveEntry && !postsForQuery) {
				output.posts = PostServiceProvider.provide().getPosts(showAll,
						input.includePostContents, input.pager.start,
						input.pager.count,
						PostSortType.fromString(input.pager.sortBy),
						input.pager.sortDirection);
			}

			if (output.posts != null) {
				Map<Key<User>, User> users = new HashMap<Key<User>, User>();

				for (Post post : output.posts) {
					if (users.get(post.authorKey) == null) {
						users.put(post.authorKey, UserHelper
								.stripSensitive(UserServiceProvider.provide()
										.getUser(
												Long.valueOf(post.authorKey
														.getId()))));
					}

					post.author = users.get(post.authorKey);
				}
			}

			output.pager = PagerHelper.moveForward(input.pager);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPosts");
		return output;
	}

	public SetupBlogResponse setupBlog (SetupBlogRequest input) {
		LOG.finer("Entering setupBlog");
		SetupBlogResponse output = new SetupBlogResponse();
		try {
			ApiValidator.notNull(input, SetupBlogRequest.class, "input");

			IPropertyService propertyService = PropertyServiceProvider
					.provide();
			if (propertyService.getNamedProperty(PropertyHelper.TITLE) != null)
				ApiValidator.throwServiceError(ServiceException.class,
						ApiError.ExistingSetup, "input.properties");

			ApiValidator.request(input, SetupBlogRequest.class);
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			ApiValidator.notNull(input.properties, Property.class,
					"input.properties");
			ApiValidator.notNull(input.users, User.class, "input.users");

			input.properties = PropertyValidator.setup(input.properties,
					"input.properties");

			propertyService.addPropertyBatch(input.properties);

			RoleServiceProvider.provide().addRole(
					RoleHelper
							.createFull(RoleHelper.ADMIN,
									RoleHelper.ADMIN_NAME,
									RoleHelper.ADMIN_DESCRIPTION));
			for (Permission permission : PermissionHelper.createAll()) {
				PermissionServiceProvider.provide().addPermission(permission);
			}

			input.users = UserValidator.validateAll(input.users, "input.users");

			for (User user : input.users) {
				// users are either admins or nothing
				user.roles = RoleValidator.lookupAll(user.roles,
						"input.users[n].roles");

				// users added at startup are verified
				user.verified = Boolean.TRUE;
			}

			UserServiceProvider.provide().addUserBatch(input.users);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting setupBlog");
		return output;
	}

	public GetTagsResponse getTags (GetTagsRequest input) {
		LOG.finer("Entering getTags");
		GetTagsResponse output = new GetTagsResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getTags");
		return output;
	}

	public UpdatePropertiesResponse updateProperties (
			UpdatePropertiesRequest input) {
		LOG.finer("Entering updateProperties");
		UpdatePropertiesResponse output = new UpdatePropertiesResponse();
		try {
			ApiValidator.notNull(input, UpdatePropertiesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");
			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, null,
					"input.session.user");

			List<Property> updatedProperties = PropertyValidator.validateAll(
					input.properties, "input.properties");

			Property existingProperty = null;
			boolean found;
			for (Property property : updatedProperties) {
				found = false;
				try {
					existingProperty = PropertyValidator.lookup(property,
							"input.properties[n]");
					found = true;
				} catch (InputValidationException ex) {
					LOG.info("Property [" + property.name
							+ "] does not exist. Will add with value ["
							+ property.value + "].");
				}

				if (found) {
					existingProperty.value = property.value;
					PropertyServiceProvider.provide().updateProperty(
							existingProperty);
				} else {
					PropertyServiceProvider.provide().addProperty(property);
				}
			}

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updateProperties");
		return output;
	}
}
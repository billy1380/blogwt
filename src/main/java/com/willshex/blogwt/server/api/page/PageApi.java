//  
//  PageApi.java
//  blogwt
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.FormValidator;
import com.willshex.blogwt.server.api.validation.PageValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.helper.EmailHelper;
import com.willshex.blogwt.server.service.PersistenceService;
import com.willshex.blogwt.server.service.page.PageServiceProvider;
import com.willshex.blogwt.server.service.permission.PermissionServiceProvider;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Field;
import com.willshex.blogwt.shared.api.datatype.FieldTypeType;
import com.willshex.blogwt.shared.api.datatype.Page;
import com.willshex.blogwt.shared.api.datatype.PageSortType;
import com.willshex.blogwt.shared.api.datatype.Permission;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.CreatePageResponse;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageResponse;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesResponse;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormResponse;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageResponse;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PermissionHelper;
import com.willshex.blogwt.shared.helper.PostHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.blogwt.shared.helper.UserHelper;
import com.willshex.gson.web.service.server.ActionHandler;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.gson.web.service.shared.StatusType;

public final class PageApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(Page.class.getName());

	public SubmitFormResponse submitForm (SubmitFormRequest input) {
		LOG.finer("Entering submitForm");
		SubmitFormResponse output = new SubmitFormResponse();
		try {
			// send an email with the submitted fields
			ApiValidator.notNull(input, UpdatePageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				output.session = input.session = SessionValidator
						.lookupAndExtend(input.session, "input.session");
			}

			input.form = FormValidator.validate(input.form, "input.form");

			Property email = PropertyServiceProvider.provide()
					.getNamedProperty(PropertyHelper.OUTGOING_EMAIL);

			Property title = PropertyServiceProvider.provide()
					.getNamedProperty(PropertyHelper.TITLE);

			StringBuffer body = new StringBuffer();
			for (Field field : input.form.fields) {
				if (field.type != FieldTypeType.FieldTypeTypeCaptcha) {
					body.append(field.name);
					body.append(":");
					body.append(field.value);
					body.append("\n\n");
				}
			}

			if (!EmailHelper.sendEmail(email.value, title.value, "Submit form",
					body.toString(), false))
				ApiValidator.throwServiceError(ServiceException.class,
						ApiError.FailedToSendEmail, "input.form");

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);

			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting submitForm");
		return output;
	}

	public UpdatePageResponse updatePage (UpdatePageRequest input) {
		LOG.finer("Entering updatePage");
		UpdatePageResponse output = new UpdatePageResponse();
		try {
			ApiValidator.notNull(input, UpdatePageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			List<Permission> permissions = new ArrayList<Permission>();
			Permission postPermission = PermissionServiceProvider.provide()
					.getCodePermission(PermissionHelper.MANAGE_PAGES);
			permissions.add(postPermission);

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, permissions,
					"input.session.user");

			Page updatedPage = input.page;

			input.page = PageValidator.lookup(input.page, "input.page");
			updatedPage = PageValidator.validate(updatedPage, "input.page");

			input.page.hasChildren = updatedPage.hasChildren;
			input.page.parent = updatedPage.parent;
			input.page.posts = updatedPage.posts;
			input.page.priority = updatedPage.priority;
			input.page.title = updatedPage.title;
			input.page.slug = PostHelper.slugify(input.page.title);

			output.page = PageServiceProvider.provide().updatePage(input.page);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting updatePage");
		return output;
	}

	public DeletePageResponse deletePage (DeletePageRequest input) {
		LOG.finer("Entering deletePage");
		DeletePageResponse output = new DeletePageResponse();
		try {
			ApiValidator.notNull(input, DeletePageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_PAGES)),
					"input.session.user");

			input.page = PageValidator.lookup(input.page, "input.page");

			PageServiceProvider.provide().deletePage(input.page);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting deletePage");
		return output;
	}

	public CreatePageResponse createPage (CreatePageRequest input) {
		LOG.finer("Entering createPage");
		CreatePageResponse output = new CreatePageResponse();
		try {
			ApiValidator.notNull(input, CreatePageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			input.session.user = UserServiceProvider.provide().getUser(
					Long.valueOf(input.session.userKey.getId()));

			UserValidator.authorisation(input.session.user, Arrays
					.asList(PermissionServiceProvider.provide()
							.getCodePermission(PermissionHelper.MANAGE_PAGES)),
					"input.session.user");

			input.page = PageValidator.validate(input.page, "input.page");
			input.page.slug = PostHelper.slugify(input.page.title);

			output.page = PageServiceProvider.provide().addPage(input.page);

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting createPage");
		return output;
	}

	public GetPagesResponse getPages (GetPagesRequest input) {
		LOG.finer("Entering getPages");
		GetPagesResponse output = new GetPagesResponse();
		try {
			ApiValidator.notNull(input, GetPagesRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			output.session = input.session = SessionValidator.lookupAndExtend(
					input.session, "input.session");

			if (input.pager == null) {
				input.pager = PagerHelper.createDefaultPager();
			}

			if (input.query == null || input.query.trim().length() == 0) {
				output.pages = PageServiceProvider.provide().getPages(
						input.includePosts, input.pager.start,
						input.pager.count,
						PageSortType.fromString(input.pager.sortBy),
						input.pager.sortDirection);
			} else {
				output.pages = PageServiceProvider.provide()
						.getPartialSlugPages(input.query, input.includePosts,
								input.pager.start, input.pager.count,
								PageSortType.fromString(input.pager.sortBy),
								input.pager.sortDirection);
			}

			Map<Long, User> owners = new HashMap<Long, User>();
			Long id;
			for (Page page : output.pages) {
				id = Long.valueOf(page.ownerKey.getId());
				page.owner = owners.get(id);
				if (page.owner == null) {
					owners.put(
							id,
							page.owner = UserHelper
									.stripSensitive(UserServiceProvider
											.provide().getUser(id)));
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
		LOG.finer("Exiting getPages");
		return output;
	}

	public GetPageResponse getPage (GetPageRequest input) {
		LOG.finer("Entering getPage");
		GetPageResponse output = new GetPageResponse();
		try {
			ApiValidator.notNull(input, GetPageRequest.class, "input");
			ApiValidator.accessCode(input.accessCode, "input.accessCode");

			if (input.session != null) {
				try {
					output.session = input.session = SessionValidator
							.lookupAndExtend(input.session, "input.session");
				} catch (InputValidationException ex) {
					output.session = input.session = null;
				}
			}

			output.page = input.page = PageValidator.lookup(input.page,
					"input.page");

			if (output.page.parentKey != null) {
				output.page.parent = PageValidator.lookup(PersistenceService
						.dataType(Page.class, output.page.parentKey),
						"input.page.parent");
			}

			if (Boolean.TRUE.equals(input.includePosts)) {
				output.page = PageServiceProvider.provide().getPage(
						input.page.id, input.includePosts);
			}

			output.page.owner = UserHelper.stripSensitive(UserServiceProvider
					.provide().getUser(
							Long.valueOf(output.page.ownerKey.getId())));

			UserHelper.stripPassword(output.session == null ? null
					: output.session.user);
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting getPage");
		return output;
	}
}
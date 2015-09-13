//
//  PageTypeHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.History;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.PageType;
import com.willshex.blogwt.client.page.admin.PagesPage;
import com.willshex.blogwt.client.page.admin.PermissionsPage;
import com.willshex.blogwt.client.page.admin.PropertiesPage;
import com.willshex.blogwt.client.page.admin.ResourcesPage;
import com.willshex.blogwt.client.page.admin.RolesPage;
import com.willshex.blogwt.client.page.admin.UsersPage;
import com.willshex.blogwt.client.page.blog.EditPostPage;
import com.willshex.blogwt.client.page.blog.PostDetailPage;
import com.willshex.blogwt.client.page.blog.PostsPage;
import com.willshex.blogwt.client.page.blog.SearchPage;
import com.willshex.blogwt.client.page.blog.SetupBlogPage;
import com.willshex.blogwt.client.page.blog.TagPage;
import com.willshex.blogwt.client.page.page.EditPagePage;
import com.willshex.blogwt.client.page.page.PageDetailPage;
import com.willshex.blogwt.client.page.user.ChangeDetailsPage;
import com.willshex.blogwt.client.page.user.ChangePasswordPage;
import com.willshex.blogwt.client.page.user.LoginPage;
import com.willshex.blogwt.client.page.user.RegisterPage;
import com.willshex.blogwt.client.page.user.ResetPasswordPage;
import com.willshex.blogwt.client.page.user.VerifyAccountPage;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageTypeHelper {
	private static Page defaultPage = null;

	/**
	 * 
	 * @param pageType
	 * @return
	 */
	public static Page createPage (PageType pageType) {
		Page page = null;

		switch (pageType) {
		case UsersPageType:
			page = new UsersPage();
			break;
		case LoginPageType:
			page = new LoginPage();
			break;
		case RegisterPageType:
			page = new RegisterPage();
			break;
		case ChangePasswordPageType:
			page = new ChangePasswordPage();
			break;
		case ResetPasswordPageType:
			page = new ResetPasswordPage();
			break;
		case RolesPageType:
			page = new RolesPage();
			break;
		case PermissionsPageType:
			page = new PermissionsPage();
			break;
		case ChangeDetailsPageType:
			page = new ChangeDetailsPage();
			break;
		case SetupBlogPageType:
			page = new SetupBlogPage();
			break;
		case EditPostPageType:
			page = new EditPostPage();
			break;
		case PostDetailPageType:
			page = new PostDetailPage();
			break;
		case PropertiesPageType:
			page = new PropertiesPage();
			break;
		case ResourcesPageType:
			page = new ResourcesPage();
			break;
		case PagesPageType:
			page = new PagesPage();
			break;
		case EditPagePageType:
			page = new EditPagePage();
			break;
		case TagPostsPageType:
			page = new TagPage();
			break;
		case LogoutPageType:
		case PostsPageType:
			page = new PostsPage();
			break;
		case SearchPostsPageType:
			page = new SearchPage();
			break;
		case VerifyAccountPageType:
			page = new VerifyAccountPage();
			break;
		case PageDetailPageType:
		default:
			if (defaultPage == null) {
				defaultPage = new PageDetailPage();
			}
			page = defaultPage;
			break;
		}

		return page;
	}

	public static void show (final PageType pageType) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute () {
				History.newItem(pageType.asTargetHistoryToken());
			}
		});
	}

	public static void show (final PageType pageType, final String... params) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute () {
				History.newItem(pageType.asTargetHistoryToken(params));
			}
		});
	}

	public static SafeUri asHref (PageType pageType) {
		return UriUtils.fromString("#" + pageType.asTargetHistoryToken());
	}

	public static SafeUri asHref (PageType pageType, String... params) {
		return UriUtils
				.fromString("#!" + pageType.asTargetHistoryToken(params));
	}
}

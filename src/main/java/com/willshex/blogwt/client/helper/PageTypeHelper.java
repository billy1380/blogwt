//
//  PageTypeHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.History;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.admin.PagesPage;
import com.willshex.blogwt.client.page.admin.PermissionsPage;
import com.willshex.blogwt.client.page.admin.PropertiesPage;
import com.willshex.blogwt.client.page.admin.ResourcesPage;
import com.willshex.blogwt.client.page.admin.RolesPage;
import com.willshex.blogwt.client.page.admin.UsersPage;
import com.willshex.blogwt.client.page.admin.WidgetTestPage;
import com.willshex.blogwt.client.page.admin.metanotifications.MetaNotificationsPage;
import com.willshex.blogwt.client.page.admin.metanotifications.detail.MetaNotificationDetailPage;
import com.willshex.blogwt.client.page.blog.EditPostPage;
import com.willshex.blogwt.client.page.blog.EditResourcePage;
import com.willshex.blogwt.client.page.blog.PostDetailPage;
import com.willshex.blogwt.client.page.blog.PostsPage;
import com.willshex.blogwt.client.page.blog.SearchPage;
import com.willshex.blogwt.client.page.blog.SetupBlogPage;
import com.willshex.blogwt.client.page.blog.TagPage;
import com.willshex.blogwt.client.page.page.EditPagePage;
import com.willshex.blogwt.client.page.page.PageDetailPage;
import com.willshex.blogwt.client.page.user.ChangeAccessPage;
import com.willshex.blogwt.client.page.user.ChangeDetailsPage;
import com.willshex.blogwt.client.page.user.ChangePasswordPage;
import com.willshex.blogwt.client.page.user.LoginPage;
import com.willshex.blogwt.client.page.user.ResetPasswordPage;
import com.willshex.blogwt.client.page.user.VerifyAccountPage;
import com.willshex.blogwt.client.page.user.downloads.DownloadsPage;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageTypeHelper {
	private static Page defaultPage = null;

	public static final SafeUri PAGES_PAGE_HREF = asHref(
			PageType.PagesPageType);
	public static final SafeUri PROPERTIES_PAGE_HREF = asHref(
			PageType.PropertiesPageType);
	public static final SafeUri USERS_PAGE_HREF = asHref(
			PageType.UsersPageType);
	public static final SafeUri ROLES_PAGE_HREF = asHref(
			PageType.RolesPageType);
	public static final SafeUri PERMISSIONS_PAGE_HREF = asHref(
			PageType.PermissionsPageType);
	public static final SafeUri RESOURCES_PAGE_HREF = asHref(
			PageType.ResourcesPageType);
	public static final SafeUri POSTS_PAGE_HREF = asHref(
			PageType.AllPostsPageType);
	public static final SafeUri DOWNLOADS_PAGE_HREF = asHref(
			PageType.DownloadsPageType);
	public static final SafeUri META_NOTIFICATIONS_PAGE_HREF = asHref(
			PageType.MetaNotificationsPageType);

	public static final SafeUri CHANGE_DETAILS_PAGE_HREF = asHref(
			PageType.ChangeDetailsPageType);
	public static final SafeUri CHANGE_PASSWORD_PAGE_HREF = asHref(
			PageType.ChangePasswordPageType);
	public static final SafeUri CHANGE_ACCESS_PAGE_HREF = asHref(
			PageType.ChangeAccessPageType);

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
		case RegisterPageType:
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
		case ChangeAccessPageType:
			page = new ChangeAccessPage();
			break;
		case EditResourcePageType:
			page = new EditResourcePage();
			break;
		case WidgetTestPageType:
			page = new WidgetTestPage();
			break;
		case AllPostsPageType:
			page = new com.willshex.blogwt.client.page.admin.PostsPage();
			break;
		case DownloadsPageType:
			page = new DownloadsPage();
			break;
		case MetaNotificationsPageType:
			page = new MetaNotificationsPage();
			break;
		case MetaNotificationDetailPageType:
			page = new MetaNotificationDetailPage();
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
		show(pageType.asTargetHistoryToken());
	}

	public static void show (final PageType pageType, final String... params) {
		show(pageType.asTargetHistoryToken(params));
	}

	public static void show (final String targetHistoryToken) {
		Scheduler.get()
				.scheduleDeferred( () -> History.newItem(targetHistoryToken));
	}

	public static SafeUri asHref (PageType pageType) {
		return UriUtils.fromString("#" + pageType.asTargetHistoryToken());
	}

	public static SafeUri asHref (PageType pageType, String... params) {
		return UriUtils.fromString("#" + pageType.asTargetHistoryToken(params));
	}

	public static SafeUri slugToHref (String slug) {
		return UriUtils.fromString("#" + slugToTargetHistoryToken(slug));
	}

	public static String slugToTargetHistoryToken (String slug) {
		return "!" + slug;
	}
}

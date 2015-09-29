//
//  PropertyHelper.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.willshex.blogwt.shared.api.datatype.Property;

/**
 * @author billy1380
 *
 */
public class PropertyHelper {
	// setup properties
	public static final String TITLE = "setup.title";
	public static final String EXTENDED_TITLE = "setup.extended.title";
	public static final String COPYRIGHT_HOLDER = "setup.copyright.holder";
	public static final String COPYRIGHT_URL = "setup.copyright.url";

	public static final String PASSWORD_HASH_SALT = "setup.password.hash.salt";

	// functional properties
	public static final String MARKDOWN_MAPS_API_KEY = "markdown.maps.api.key";
	public static final String MARKDOWN_PREFETCH_INCLUDES = "markdown.include.prefectch";

	public static final String POST_COMMENTS_ENABLED = "post.comments.enabled";
	public static final String POST_DISQUS_ID = "post.comments.disqus.id";
	public static final String POST_CATEGORY_ID = "post.comments.disqus.category.id";

	public static final String POST_SHARE_ENABLED = "post.share.enabled";

	public static final String POST_ENABLE_EMOJI = "post.enable.emoji";
	public static final String POST_SHOW_AUTHOR = "post.show.author";
	public static final String POST_SHOW_AUTHOR_SUMMARY = "post.show.author.summary";

	public static final String SHOW_SIGN_IN = "header.show.signin";

	public static final String GENERATE_RSS_FEED = "content.rss.generate";
	public static final String SHORT_DESCRIPTION = "content.short.description";

	public static final String SMALL_LOGO_URL = "content.small.logo.url";
	public static final String LARGE_LOGO_URL = "content.large.logo.url";
	public static final String FAVICON_URL = "content.favicon.url";

	public static final String FOOTER_SHOW_VERSION = "footer.show.site.version";

	public static final String OUTGOING_EMAIL = "email.outgoing";

	public static final String COOKIE_DETAILS_PAGE_SLUG = "cookie.details.slug";

	// user properties
	public static final String ALLOW_USER_REGISTRATION = "user.allow.registration";
	public static final String NEW_USER_PERMISSIONS = "user.new.permissions";
	public static final String NEW_USER_ROLES = "user.new.roles";

	// Descriptions
	public static final String TITLE_DESCRIPTION = "Blog Title";
	public static final String EXTENDED_TITLE_DESCRIPTION = "Extended Title/Sub Title";
	public static final String COPYRIGHT_HOLDER_DESCRIPTION = "Copyright Holder Name";
	public static final String COPYRIGHT_URL_DESCRIPTION = "Copyright Url";

	public static final String PASSWORD_HASH_SALT_DESCRIPTION = "Salt for user password hashing, all accounts will be locked on change";

	public static final String MARKDOWN_MAPS_API_KEY_DESCRIPTION = "Google Maps API Key";
	public static final String MARKDOWN_PREFETCH_INCLUDES_DESCIPTION = "Fetch markdown includes on the server before returning mardkown content";

	public static final String POST_COMMENTS_ENABLED_DESCRIPTION = "Enable Comments";
	public static final String POST_DISQUS_ID_DESCRIPTION = "Disqus Identifier";
	public static final String POST_CATEGORY_ID_DESCRIPTION = "Disqus Category Identifier";

	public static final String POST_SHARE_ENABLED_DESCRIPTION = "Enable sharing in posts";

	public static final String POST_ENABLE_EMOJI_DESCRIPTION = "Enable emoji in posts and on pages";
	public static final String POST_SHOW_AUTHOR_DESCRIPTION = "Show the authors on the post and post summaries";
	public static final String POST_SHOW_AUTHOR_SUMMARY_DESCRIPTION = "Shows the author summary on post details";

	public static final String SHORT_DESCRIPTION_DESCRIPTION = "A short description of the blog";

	public static final String SHOW_SIGN_IN_DESCRIPTION = "Show the sign in button on the top right hand of the header";

	public static final String GENERATE_RSS_FEED_DESCRIPTION = "Enables RSS feed generation";

	public static final String SMALL_LOGO_URL_DESCRIPTION = "A small logo to display on the blog header";
	public static final String LARGE_LOGO_URL_DESCRIPTION = "A large logo to display anywhere on the site";
	public static final String FAVICON_URL_DESCRIPTION = "A favicon (.ico) for the site";

	public static final String FOOTER_SHOW_VERSION_DESCRIPTION = "Shows the blogwt version on the site footer";
	public static final String OUTGOING_EMAIL_DESCRIPTION = "Appengine outgoing email address";

	public static final String ALLOW_USER_REGISTRATION_DESCRIPTION = "Allow users to register";
	public static final String NEW_USER_PERMISSIONS_DESCRIPTION = "New user permissions";
	public static final String NEW_USER_ROLES_DESCRIPTION = "New user roles";

	public static final String COOKIE_DETAILS_PAGE_SLUG_DESCRIPTION = "Cookie details page slug";

	public static final String NONE_VALUE = "none";
	public static final String APPLE_VALUE = "apple";
	public static final String NOTO_VALUE = "noto";
	public static final String TWITTER_VALUE = "twitter";

	private static List<Property> properties = null;

	public static Map<String, Property> toLookup (List<Property> properties) {
		Map<String, Property> lookup = new HashMap<String, Property>();
		for (Property property : properties) {
			lookup.put(property.name, property);
		}
		return lookup;
	}

	public static Property createTitle (String value) {
		return new Property().name(TITLE).description(TITLE_DESCRIPTION)
				.value(value).group("Setup").type("string");
	}

	public static Property createExtendedTitle (String value) {
		return new Property().name(EXTENDED_TITLE)
				.description(EXTENDED_TITLE_DESCRIPTION).value(value)
				.group("Setup").type("string");
	}

	public static Property createCopyrightHolder (String value) {
		return new Property().name(COPYRIGHT_HOLDER)
				.description(COPYRIGHT_HOLDER_DESCRIPTION).value(value)
				.group("Setup").type("string");
	}

	public static Property createCopyrightUrl (String value) {
		return new Property().name(COPYRIGHT_URL)
				.description(COPYRIGHT_URL_DESCRIPTION).value(value)
				.group("Setup").type("string");
	}

	public static Property createPasswordHashSalt (String value) {
		return new Property().name(PASSWORD_HASH_SALT)
				.description(PASSWORD_HASH_SALT_DESCRIPTION).value(value)
				.group("Setup").type("string");
	}

	public static Property createPostEnableEmoji (String value) {
		return new Property().name(POST_ENABLE_EMOJI)
				.description(POST_ENABLE_EMOJI_DESCRIPTION).value(value)
				.group("Functional").type("string");
	}

	public static Property createPostCommentsEnabled (String value) {
		return new Property().name(POST_COMMENTS_ENABLED)
				.description(POST_COMMENTS_ENABLED_DESCRIPTION)
				.group("Functional").type("string").value(value);
	}

	public static Property createPostShareEnabled (String value) {
		return new Property().name(POST_SHARE_ENABLED)
				.description(POST_SHARE_ENABLED_DESCRIPTION)
				.group("Functional").type("string").value(value);
	}

	public static Property createMarkdownMapsApiKey (String value) {
		return new Property().name(MARKDOWN_MAPS_API_KEY)
				.description(MARKDOWN_MAPS_API_KEY_DESCRIPTION)
				.group("Functional").type("string").value(value);
	}

	public static Property createDisqusId (String value) {
		return new Property().name(POST_DISQUS_ID)
				.description(POST_DISQUS_ID_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createCategoryId (String value) {
		return new Property().name(POST_CATEGORY_ID)
				.description(POST_CATEGORY_ID_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createMarkdownPrefechIncludes (Boolean value) {
		return new Property().name(MARKDOWN_PREFETCH_INCLUDES)
				.description(MARKDOWN_PREFETCH_INCLUDES_DESCIPTION)
				.group("Functional").type("boolean")
				.value(value == null ? null : value.toString());
	}

	public static Property createPostShowAuthor (Boolean value) {
		return new Property().name(POST_SHOW_AUTHOR)
				.description(POST_SHOW_AUTHOR_DESCRIPTION).group("Functional")
				.type("boolean").value(value == null ? null : value.toString());
	}

	public static Property createPostShowAuthorSummary (Boolean value) {
		return new Property().name(POST_SHOW_AUTHOR_SUMMARY)
				.description(POST_SHOW_AUTHOR_SUMMARY_DESCRIPTION)
				.group("Functional").type("boolean")
				.value(value == null ? null : value.toString());
	}

	public static Property createShortDescription (String value) {
		return new Property().name(SHORT_DESCRIPTION)
				.description(SHORT_DESCRIPTION_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createShowSignIn (Boolean value) {
		return new Property().name(SHOW_SIGN_IN)
				.description(SHOW_SIGN_IN_DESCRIPTION).group("Functional")
				.type("boolean").value(value == null ? null : value.toString());
	}

	public static Property createGenerateRssFeed (Boolean value) {
		return new Property().name(GENERATE_RSS_FEED)
				.description(GENERATE_RSS_FEED_DESCRIPTION).group("Functional")
				.type("boolean").value(value == null ? null : value.toString());
	}

	public static Property createSmallLogoUrl (String value) {
		return new Property().name(SMALL_LOGO_URL)
				.description(SMALL_LOGO_URL_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createLargeLogoUrl (String value) {
		return new Property().name(LARGE_LOGO_URL)
				.description(LARGE_LOGO_URL_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createFooterShowVersion (Boolean value) {
		return new Property().name(FOOTER_SHOW_VERSION)
				.description(FOOTER_SHOW_VERSION_DESCRIPTION)
				.group("Functional").type("boolean")
				.value(value == null ? null : value.toString());
	}

	public static Property createAllowUserRegistration (Boolean value) {
		return new Property().name(ALLOW_USER_REGISTRATION)
				.description(ALLOW_USER_REGISTRATION_DESCRIPTION).group("User")
				.type("boolean").value(value == null ? null : value.toString());
	}

	public static Property createFaviconUrl (String value) {
		return new Property().name(FAVICON_URL)
				.description(FAVICON_URL_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createNewUserPermissions (String value) {
		return new Property().name(NEW_USER_PERMISSIONS)
				.description(NEW_USER_PERMISSIONS_DESCRIPTION).group("User")
				.type("string").value(value);
	}

	public static Property createNewUserRoles (String value) {
		return new Property().name(NEW_USER_ROLES)
				.description(NEW_USER_ROLES_DESCRIPTION).group("User")
				.type("string").value(value);
	}

	public static Property createOutgoingEmail (String value) {
		return new Property().name(OUTGOING_EMAIL)
				.description(OUTGOING_EMAIL_DESCRIPTION).group("Functional")
				.type("string").value(value);
	}

	public static Property createCookieDetailsPageSlug (String value) {
		return new Property().name(COOKIE_DETAILS_PAGE_SLUG)
				.description(COOKIE_DETAILS_PAGE_SLUG_DESCRIPTION)
				.group("Functional").type("string").value(value);
	}

	public static boolean isEmpty (Property property) {
		return property == null || property.value == null
				|| property.value.length() == 0;
	}

	/**
	 * @return
	 */
	public static List<Property> properties () {
		if (properties == null) {
			properties = Arrays.asList(createTitle(null),
					createExtendedTitle(null), createCopyrightHolder(null),
					createCopyrightUrl(null), createPasswordHashSalt(null),
					createPostEnableEmoji(null),
					createPostCommentsEnabled(null),
					createMarkdownMapsApiKey(null), createDisqusId(null),
					createCategoryId(null), createPostShareEnabled(null),
					createMarkdownPrefechIncludes(null),
					createPostShowAuthor(null),
					createPostShowAuthorSummary(null),
					createShortDescription(null), createShowSignIn(null),
					createGenerateRssFeed(null), createSmallLogoUrl(null),
					createLargeLogoUrl(null), createFooterShowVersion(null),
					createAllowUserRegistration(null),
					createNewUserPermissions(null), createNewUserRoles(null),
					createFaviconUrl(null), createOutgoingEmail(null),
					createCookieDetailsPageSlug(null));
		}

		return properties;
	}

	/**
	 * @param property
	 * @return
	 */
	public static String value (Property property) {
		return property == null ? null : property.value;
	}
}

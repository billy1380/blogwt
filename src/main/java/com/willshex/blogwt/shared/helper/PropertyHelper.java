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
	
	public static final String POST_ENABLE_EMOJI = "post.enable.emoji";
	public static final String POST_SHOW_AUTHOR = "post.show.author";
	public static final String POST_SHOW_AUTHOR_SUMMARY = "post.show.author.summary";
	
	
	public static final String SHOW_SIGN_IN = "header.show.signin";
	
	public static final String GENERATE_RSS_FEED = "content.rss.generate";
	public static final String SHORT_DESCRIPTION = "content.short.description";
	
	public static final String SMALL_LOGO_URL = "content.small.logo.url";
	public static final String LARGE_LOGO_URL = "content.large_logo_url";
	
	public static final String EMOJI_THEME = "content.emoji.theme";
	
	public static final String FOOTER_SHOW_VERSION= "footer.show.site.version";
	
	// user properties
	public static final String ALLOW_USER_REGISTRATION = "user.allow.registration";
	
	

	public static final String TITLE_DESCRIPTION = "Blog Title";
	public static final String EXTENDED_TITLE_DESCRIPTION = "Extended Title (Short Description)";
	public static final String COPYRIGHT_HOLDER_DESCRIPTION = "Copyright Holder Name";
	public static final String COPYRIGHT_URL_DESCRIPTION = "Copyright Url";

	public static final String MARKDOWN_MAPS_API_KEY_DESCRIPTION = "Google Maps API Key";
	public static final String POST_COMMENTS_ENABLED_DESCRIPTION = "Enable Comments";
	public static final String POST_DISQUS_ID_DESCRIPTION = "Disqus Identifier";
	public static final String POST_CATEGORY_ID_DESCRIPTION = "Disqus Category Identifier";

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

	public static boolean isEmpty (Property property) {
		return property == null || property.value == null
				|| property.value.length() == 0;
	}

	/**
	 * @return
	 */
	public static List<Property> properties () {
		if (properties == null) {
			properties = Arrays.asList(
					createTitle(null),
					createExtendedTitle(null),
					createCopyrightHolder(null),
					createCopyrightUrl(null),
					new Property().name(MARKDOWN_MAPS_API_KEY)
							.description(MARKDOWN_MAPS_API_KEY_DESCRIPTION)
							.group("Functional").type("string"),
					new Property().name(POST_COMMENTS_ENABLED)
							.description(POST_COMMENTS_ENABLED_DESCRIPTION)
							.group("Functional").type("string"),
					new Property().name(POST_DISQUS_ID)
							.description(POST_DISQUS_ID_DESCRIPTION)
							.group("Functional").type("string"),
					new Property().name(POST_CATEGORY_ID)
							.description(POST_CATEGORY_ID_DESCRIPTION)
							.group("Functional").type("string"));
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

//
//  PostHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.List;

import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelper {

	private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyz_0123456789";
	private static final char REPLACEMENT_CHAR = '_';
	private static final int MAX_CODE_LENGTH = 45;

	public static String slugify (String title) {
		StringBuffer slug = new StringBuffer();

		if (title != null && title.length() > 0) {
			title = title.toLowerCase();

			int size = Math.min(title.length(), MAX_CODE_LENGTH);
			char c;
			boolean replacedOne = false;
			for (int i = 0; i < size; i++) {
				c = title.charAt(i);

				if (ALLOWED_CHARS.contains(Character.toString(c))) {
					slug.append(c);
					replacedOne = false;
				} else if (!replacedOne) {
					slug.append(REPLACEMENT_CHAR);
					replacedOne = true;
				}
			}
		}

		return slug.toString();
	}

	public static String getSlug (Post post) {
		return post.slug == null || post.slug.length() == 0 ? post.id.toString()
				: post.slug;
	}

	/**
	 * @param posts
	 * @param slug
	 * @return
	 */
	public static String nextPostSlug (List<Post> posts, String slug) {
		String nextSlug = slug;

		if (posts != null) {
			String strippedSlug;
			int largestSuffix = 0, suffix;
			for (Post post : posts) {
				strippedSlug = post.slug.replace(slug, "");

				if (!strippedSlug.isEmpty()) {
					suffix = Integer.parseInt(strippedSlug);

					if (suffix > largestSuffix) {
						largestSuffix = suffix;
					}
				}
			}

			if (posts.size() > 0 && largestSuffix >= 0) {
				nextSlug += Integer.toString(largestSuffix + 1);
			}
		}

		return nextSlug;
	}

}

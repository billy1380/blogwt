//
//  PostHelperTest.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.willshex.blogwt.shared.api.datatype.Post;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelperTest {

	@Test
	public void testSlugify () {
		String slug = PostHelper.slugify("aAB~sjdf0+q398754k'@@lja£%$£");

		Assert.assertTrue("aab_sjdf0_q398754k_lja_".equals(slug));
	}

	@Test
	public void testNextPostSlug () {
		// duplicate
		{
			List<Post> posts = Arrays.asList(new Post().slug("test"));
			String nextSlug = PostHelper.nextPostSlug(posts, "test");

			Assert.assertTrue("test1".equals(nextSlug));
		}

		// more than one
		{
			List<Post> posts = Arrays.asList(new Post().slug("test"),
					new Post().slug("test1"));
			String nextSlug = PostHelper.nextPostSlug(posts, "test");

			Assert.assertTrue("test2".equals(nextSlug));
		}

		// no empty
		{
			List<Post> posts = Arrays.asList(new Post().slug("test3"));
			String nextSlug = PostHelper.nextPostSlug(posts, "test");

			Assert.assertTrue("test4".equals(nextSlug));
		}

		// unordered
		{
			List<Post> posts = Arrays.asList(new Post().slug("test"),
					new Post().slug("test5"), new Post().slug("test3"),
					new Post().slug("test1"));
			String nextSlug = PostHelper.nextPostSlug(posts, "test");

			Assert.assertTrue("test6".equals(nextSlug));
		}

		// double digit
		{
			List<Post> posts = Arrays.asList(new Post().slug("test"),
					new Post().slug("test5"), new Post().slug("test13"),
					new Post().slug("test1"));
			String nextSlug = PostHelper.nextPostSlug(posts, "test");

			Assert.assertTrue("test14".equals(nextSlug));
		}
	}

}

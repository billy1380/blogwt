//
//  PostHelperTest.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 12 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import org.junit.Assert;
import org.junit.Test;

import com.willshex.blogwt.shared.helper.PostHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelperTest {

	@Test
	public void testSlugify() {
		String slug = PostHelper.slugify("aAB~sjdf0+q398754k'@@lja£%$£");
		
		Assert.assertTrue("aab_sjdf0_q398754k_lja_".equals(slug));
	}
	
}

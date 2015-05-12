//
//  PostHelperTest.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 12 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.shared.api.helper;

import org.junit.Assert;
import org.junit.Test;

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

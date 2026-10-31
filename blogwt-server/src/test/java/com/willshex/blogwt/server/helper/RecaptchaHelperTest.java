//
//  RecaptchaHelperTest.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 31 Oct 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RecaptchaHelperTest {

	@Test
	public void testConfiguredSiteKeyWins () {
		Assert.assertEquals("configured",
				RecaptchaHelper.siteKey("configured", "submitted"));
	}

	@Test
	public void testConfiguredSiteKeyIsTrimmed () {
		Assert.assertEquals("configured",
				RecaptchaHelper.siteKey("  configured\n", "submitted"));
	}

	@Test
	public void testSubmittedSiteKeyUsedWhenNothingConfigured () {
		Assert.assertEquals("submitted",
				RecaptchaHelper.siteKey(null, "submitted"));
		Assert.assertEquals("submitted",
				RecaptchaHelper.siteKey("", "submitted"));
		Assert.assertEquals("submitted",
				RecaptchaHelper.siteKey("   ", "submitted"));
	}

	@Test
	public void testNoSiteKeyAtAll () {
		Assert.assertNull(RecaptchaHelper.siteKey(null, null));
	}

}

//
//  PropertyHelperTest.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Oct 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.shared.helper;

import org.junit.Assert;
import org.junit.Test;

import com.willshex.blogwt.shared.api.datatype.Property;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertyHelperTest {

	@Test
	public void testBrevoKeyIsSecret () {
		Assert.assertTrue(PropertyHelper
				.isSecretProperty(PropertyHelper.createBrevoApiKey("key")));
	}

	@Test
	public void testBrevoSwitchIsNotSecret () {
		Assert.assertFalse(PropertyHelper
				.isSecretProperty(PropertyHelper.createBrevoEnabled(Boolean.TRUE)));
	}

	@Test
	public void testBrevoPropertiesAreListed () {
		boolean enabled = false, key = false;

		for (Property property : PropertyHelper.properties()) {
			enabled |= PropertyHelper.BREVO_ENABLED.equals(property.name);
			key |= PropertyHelper.BREVO_API_KEY.equals(property.name);
		}

		Assert.assertTrue(enabled);
		Assert.assertTrue(key);
	}

	@Test
	public void testRecaptchaSiteKeyIsPublicAndListed () {
		Assert.assertFalse(PropertyHelper.isSecretProperty(
				PropertyHelper.createRecaptchaSiteKey("key")));
		Assert.assertTrue(PropertyHelper.isSecretProperty(
				PropertyHelper.createRecaptchaApiKey("key")));

		boolean listed = false;
		for (Property property : PropertyHelper.properties()) {
			listed |= PropertyHelper.RECAPTCHA_SITE_KEY.equals(property.name);
		}
		Assert.assertTrue(listed);
	}

	@Test
	public void testBrevoSwitchIsBoolean () {
		Property property = PropertyHelper.createBrevoEnabled(Boolean.TRUE);

		Assert.assertEquals("boolean", property.type);
		Assert.assertEquals("true", property.value);
	}

}

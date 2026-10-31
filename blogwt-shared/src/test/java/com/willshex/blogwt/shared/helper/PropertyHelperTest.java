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
	public void testRecaptchaSiteKeyIsMaskedNotSecretAndInSetup () {
		Assert.assertFalse(PropertyHelper.isSecretProperty(
				PropertyHelper.createRecaptchaSiteKey("key")));
		Assert.assertTrue(PropertyHelper.isMaskedProperty(
				PropertyHelper.createRecaptchaSiteKey("key")));
		Assert.assertTrue(PropertyHelper.isMaskedProperty(
				PropertyHelper.createBrevoApiKey("key")));

		Property listed = null;
		for (Property property : PropertyHelper.properties()) {
			if (PropertyHelper.RECAPTCHA_SITE_KEY.equals(property.name)) {
				listed = property;
			}
			Assert.assertNotEquals("recaptcha.api.key", property.name);
		}
		Assert.assertNotNull(listed);
		Assert.assertEquals("Setup", listed.group);
	}

	@Test
	public void testBlankSecretIsNotSaved () {
		Assert.assertTrue(PropertyHelper.isBlankSecret(
				new Property().name(PropertyHelper.PASSWORD_HASH_SALT)
						.value("")));
		Assert.assertTrue(PropertyHelper.isBlankSecret(
				new Property().name(PropertyHelper.BREVO_API_KEY)));
		Assert.assertFalse(PropertyHelper.isBlankSecret(
				PropertyHelper.createBrevoApiKey("key")));
		Assert.assertFalse(PropertyHelper.isBlankSecret(
				new Property().name(PropertyHelper.TITLE).value("")));
	}

	@Test
	public void testBrevoSwitchIsBoolean () {
		Property property = PropertyHelper.createBrevoEnabled(Boolean.TRUE);

		Assert.assertEquals("boolean", property.type);
		Assert.assertEquals("true", property.value);
	}

}

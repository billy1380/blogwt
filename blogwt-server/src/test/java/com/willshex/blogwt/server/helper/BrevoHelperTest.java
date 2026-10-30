//
//  BrevoHelperTest.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 Oct 2026.
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.lang.reflect.Proxy;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.service.property.IPropertyService;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.service.ServiceDiscovery;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BrevoHelperTest {

	private static void properties (String enabled, String key,
			String outgoing) {
		Map<String, String> values = new HashMap<>();
		values.put(PropertyHelper.BREVO_ENABLED, enabled);
		values.put(PropertyHelper.BREVO_API_KEY, key);
		values.put(PropertyHelper.OUTGOING_EMAIL, outgoing);

		ServiceDiscovery.registerService((IPropertyService) Proxy
				.newProxyInstance(IPropertyService.class.getClassLoader(),
						new Class<?>[] { IPropertyService.class },
						(proxy, method, args) -> {
							if ("getName".equals(method.getName())) {
								return IPropertyService.NAME;
							} else if ("getNamedProperty"
									.equals(method.getName())) {
								String value = values.get(args[0]);
								return value == null ? null
										: new Property().name((String) args[0])
												.value(value);
							}

							throw new UnsupportedOperationException(
									method.getName());
						}));
	}

	@After
	public void clear () {
		properties(null, null, null);
	}

	@Test
	public void testPayloadHtml () {
		JsonObject payload = BrevoHelper.payload("from@example.com", "From",
				"to@example.com", "To", "Subject", "<p>Body</p>", true, null);

		Assert.assertEquals("from@example.com", payload.getAsJsonObject("sender")
				.get("email").getAsString());
		Assert.assertEquals("From", payload.getAsJsonObject("sender")
				.get("name").getAsString());
		Assert.assertEquals("to@example.com", payload.getAsJsonArray("to")
				.get(0).getAsJsonObject().get("email").getAsString());
		Assert.assertEquals("To", payload.getAsJsonArray("to").get(0)
				.getAsJsonObject().get("name").getAsString());
		Assert.assertEquals("Subject", payload.get("subject").getAsString());
		Assert.assertEquals("<p>Body</p>",
				payload.get("htmlContent").getAsString());
		Assert.assertFalse(payload.has("textContent"));
		Assert.assertFalse(payload.has("attachment"));
	}

	@Test
	public void testPayloadText () {
		JsonObject payload = BrevoHelper.payload("from@example.com", "From",
				"to@example.com", "To", "Subject", "Body", false, null);

		Assert.assertEquals("Body", payload.get("textContent").getAsString());
		Assert.assertFalse(payload.has("htmlContent"));
	}

	@Test
	public void testPayloadOmitsEmptyNames () {
		JsonObject payload = BrevoHelper.payload("from@example.com", null,
				"to@example.com", "", "Subject", "Body", false, null);

		Assert.assertFalse(payload.getAsJsonObject("sender").has("name"));
		Assert.assertFalse(payload.getAsJsonArray("to").get(0)
				.getAsJsonObject().has("name"));
	}

	@Test
	public void testPayloadAttachments () {
		Map<String, byte[]> attachments = new LinkedHashMap<>();
		attachments.put("one.pdf", new byte[] { 1, 2, 3 });
		attachments.put("two.pdf", new byte[] { 4, 5 });

		JsonObject payload = BrevoHelper.payload("from@example.com", "From",
				"to@example.com", "To", "Subject", "Body", false, attachments);

		Assert.assertEquals(2, payload.getAsJsonArray("attachment").size());
		JsonObject first = payload.getAsJsonArray("attachment").get(0)
				.getAsJsonObject();
		Assert.assertEquals("one.pdf", first.get("name").getAsString());
		Assert.assertArrayEquals(new byte[] { 1, 2, 3 }, Base64.getDecoder()
				.decode(first.get("content").getAsString()));
	}

	@Test
	public void testEnabledNeedsSwitchAndKey () {
		properties("true", "key", "from@example.com");
		Assert.assertTrue(BrevoHelper.isEnabled());
	}

	@Test
	public void testDisabledWhenSwitchOff () {
		properties("false", "key", "from@example.com");
		Assert.assertFalse(BrevoHelper.isEnabled());
	}

	@Test
	public void testDisabledWhenSwitchMissing () {
		properties(null, "key", "from@example.com");
		Assert.assertFalse(BrevoHelper.isEnabled());
	}

	@Test
	public void testDisabledWhenKeyMissing () {
		properties("true", null, "from@example.com");
		Assert.assertFalse(BrevoHelper.isEnabled());

		properties("true", "", "from@example.com");
		Assert.assertFalse(BrevoHelper.isEnabled());
	}

	@Test
	public void testNothingSentWithoutOutgoingAddress () {
		properties("true", "key", null);

		Assert.assertFalse(EmailHelper.sendEmail("to@example.com", "To",
				"Subject", "Body", false));
	}

}

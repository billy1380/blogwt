package com.willshex.blogwt.server.api.blog.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public class PropertiesVersionTest {

	@Test
	public void testReadsImplementationBuildFromManifest () throws IOException {
		String manifest = "Manifest-Version: 1.0\r\nImplementation-Build: e071042f08aae66c\r\n\r\n";
		String build = GetPropertiesActionHandler.readImplementationBuild(
				new ByteArrayInputStream(manifest.getBytes(StandardCharsets.UTF_8)));

		assertEquals("e071042f08aae66c", build);
	}

	@Test
	public void testMissingManifestOrBuildReturnsNull () throws IOException {
		assertNull(GetPropertiesActionHandler.readImplementationBuild(null));

		String empty = "Manifest-Version: 1.0\r\n\r\n";
		assertNull(GetPropertiesActionHandler.readImplementationBuild(
				new ByteArrayInputStream(empty.getBytes(StandardCharsets.UTF_8))));
	}

	@Test
	public void testAppendsAndReplacesBuildVersionOnFirstPageOnly () {
		List<Property> props = new ArrayList<>();
		props.add(new Property().name(PropertyHelper.TITLE).value("Willshex"));
		props.add(new Property().name(PropertyHelper.VERSION).value("stale"));

		GetPropertiesActionHandler.appendBuildVersion(props, 0, "abc1234567");

		assertEquals(2, props.size());
		assertEquals(PropertyHelper.VERSION, props.get(1).name);
		assertEquals("abc1234567", props.get(1).value);

		List<Property> pageTwo = new ArrayList<>();
		GetPropertiesActionHandler.appendBuildVersion(pageTwo, 10, "abc1234567");
		assertTrue(pageTwo.isEmpty());
	}

	@Test
	public void testUpdateSkipsSystemVersionAndBlankSecrets () {
		assertTrue(UpdatePropertiesActionHandler.shouldSkip(
				new Property().name(PropertyHelper.VERSION).value("abc1234")));
		assertTrue(UpdatePropertiesActionHandler.shouldSkip(
				new Property().name(PropertyHelper.BREVO_API_KEY).value("")));
		assertFalse(UpdatePropertiesActionHandler.shouldSkip(
				new Property().name(PropertyHelper.TITLE).value("Willshex")));
	}

}

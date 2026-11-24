package com.willshex.blogwt.server.helper;

import org.junit.Assert;
import org.junit.Test;

import com.willshex.blogwt.shared.api.datatype.Resource;

public class ThumbnailHelperTest {

	private static Resource resource (String data, String properties) {
		Resource resource = new Resource();
		resource.data = data;
		resource.properties = properties;

		return resource;
	}

	@Test
	public void storedDataIsABucketPathOrABlobKey () {
		Assert.assertEquals("bucket/upload/a.png", ThumbnailHelper
				.storedOf(resource("gs://bucket/upload/a.png", null)));
		Assert.assertEquals("AMIfv9key",
				ThumbnailHelper.storedOf(resource("gs://AMIfv9key", null)));
		Assert.assertEquals("AMIfv9key",
				ThumbnailHelper.storedOf(resource("AMIfv9key", null)));
		Assert.assertNull(ThumbnailHelper.storedOf(resource("", null)));
	}

	@Test
	public void malformedPropertiesAreNotAnObject () {
		Assert.assertNull(
				ThumbnailHelper.propertiesOf(resource("x", "{image:foo")));
		Assert.assertNull(ThumbnailHelper.propertiesOf(resource("x", "[1]")));
		Assert.assertTrue(ThumbnailHelper.propertiesOf(resource("x", null))
				.entrySet().isEmpty());
		Assert.assertFalse(ThumbnailHelper
				.notAnImage(resource("x", "{\"contentType\":\"image/png\"}")));
		Assert.assertTrue(ThumbnailHelper.notAnImage(
				resource("x", "{\"contentType\":\"application/pdf\"}")));
	}
}

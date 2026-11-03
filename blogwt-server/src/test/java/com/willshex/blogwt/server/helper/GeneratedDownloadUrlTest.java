//
//  GeneratedDownloadUrlTest.java
//  willshex
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.util.Arrays;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.junit.Assert;
import org.junit.Test;

import com.willshex.blogwt.server.api.download.DownloadJsonServlet;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;

/**
 * A finished download is given an address; something has to answer it, or the
 * site's own page does, as a not found.
 * 
 * @author William Shakour (billy1380)
 *
 */
public class GeneratedDownloadUrlTest {

	private static List<String> served () {
		return Arrays.asList(DownloadJsonServlet.class
				.getAnnotation(WebServlet.class).urlPatterns());
	}

	@Test
	public void testTheAddressOfADownloadIsServed () {
		GeneratedDownload download = new GeneratedDownload();
		download.id = Long.valueOf(5189291651104768L);

		String url = GeneratedDownloadHelper.url(download);

		Assert.assertEquals("/api/download?action=download&id=5189291651104768",
				url);
		Assert.assertTrue(served().contains(url.substring(0, url.indexOf('?'))));
	}

	@Test
	public void testDownloadsAlreadyMadeAreStillServed () {
		Assert.assertTrue(served().contains("/download"));
		Assert.assertEquals("/download", DownloadJsonServlet.LEGACY_FILE_PATH);
	}
}

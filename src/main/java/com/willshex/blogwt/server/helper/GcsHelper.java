//
//  GcsHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GcsHelper {

	private static final Logger LOG = Logger
			.getLogger(GcsHelper.class.getName());

	public static final String BUCKET_NAME_PROPERTY_KEY = "gcs.bucket.name";
	public static final int BUFFER_SIZE = 2 * 1024 * 1024;

	public static void delete (String path) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Deleting file at [" + path + "]");
		}

		GcsFilename fileName = new GcsFilename(
				System.getProperty(BUCKET_NAME_PROPERTY_KEY), path);
		try {
			GcsServiceFactory.createGcsService().delete(fileName);
		} catch (IOException e) {
			LOG.log(Level.SEVERE,
					"Error occured trying to delete [" + path + "]", e);
		}
	}

	public static void save (byte[] data, Supplier<String> mime, String path) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Saving data with mime type [" + mime + "] to [" + path
					+ "]");
		}

		GcsFileOptions instance = new GcsFileOptions.Builder()
				.mimeType(mime.get()).build();
		GcsFilename fileName = new GcsFilename(
				System.getProperty(BUCKET_NAME_PROPERTY_KEY), path);

		try (BufferedOutputStream osw = new BufferedOutputStream(
				Channels.newOutputStream(GcsServiceFactory.createGcsService()
						.createOrReplace(fileName, instance)))) {
			osw.write(data);
		} catch (IOException e) {
			LOG.log(Level.SEVERE,
					"Error occured trying to write data to [" + path + "]", e);
		}
	}

	/**
	 * @param jsonFilename
	 * @return
	 */
	public static byte[] load (String path) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Loading file from [" + path + "]");
		}

		GcsFilename fileName = new GcsFilename(
				System.getProperty(BUCKET_NAME_PROPERTY_KEY), path);

		byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		byte[] data = null;

		try (BufferedInputStream bis = new BufferedInputStream(
				Channels.newInputStream(GcsServiceFactory.createGcsService()
						.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE)));
				ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			while ((read = bis.read(buffer)) > 0) {
				baos.write(buffer, 0, read);
			}

			data = baos.toByteArray();
		} catch (IOException e) {
			LOG.log(Level.SEVERE,
					"Error occured trying to read data from [" + path + "]", e);
		}

		return data;
	}
}

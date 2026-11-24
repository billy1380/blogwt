//
//  ThumbnailHelper.java
//  blogwt
//
//  Copyright © 2026 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * Makes and removes the thumbnail of an uploaded image resource: a scaled-down
 * copy, the same shape, stored next to the original. The resource's
 * <code>properties</code> then carry <code>thumbnail</code> (where it is
 * stored) and <code>thumbnailStaticUrl</code> (where it is served from) beside
 * <code>staticUrl</code>.
 */
public class ThumbnailHelper {

	private static final Logger LOG = Logger
			.getLogger(ThumbnailHelper.class.getName());

	/** The longest side of a thumbnail in pixels. */
	public static final int SIZE = 200;

	public static final String SUFFIX = "-thumbnail.png";
	public static final String THUMBNAIL = "thumbnail";
	public static final String THUMBNAIL_STATIC_URL = "thumbnailStaticUrl";

	/** "bucket/object" of a stored resource, or null if it is not in cloud storage. */
	static String gsPathOf (Resource resource) {
		return resource == null || resource.data == null
				|| !resource.data.startsWith("gs://") ? null
						: resource.data.substring("gs://".length());
	}

	static GcsFilename filenameOf (String gsPath) {
		int slash = gsPath.indexOf('/');

		return new GcsFilename(gsPath.substring(0, slash),
				gsPath.substring(slash + 1));
	}

	public static JsonObject propertiesOf (Resource resource) {
		return resource.properties == null || resource.properties.isEmpty()
				? new JsonObject()
				: JsonParser.parseString(resource.properties).getAsJsonObject();
	}

	static byte[] read (GcsFilename file) throws IOException {
		GcsService gcs = GcsServiceFactory.createGcsService();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (InputStream in = Channels
				.newInputStream(gcs.openReadChannel(file, 0))) {
			byte[] buffer = new byte[64 * 1024];
			int read;

			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
		}

		return out.toByteArray();
	}

	static String servingUrl (String gsPath) {
		BlobKey key = BlobstoreServiceFactory.getBlobstoreService()
				.createGsBlobKey("/gs/" + gsPath);

		return ImagesServiceFactory.getImagesService()
				.getServingUrl(ServingUrlOptions.Builder.withBlobKey(key))
				.replaceFirst("https:\\/\\/", "//")
				.replaceFirst("http:\\/\\/", "//");
	}

	/** Makes the thumbnail, or makes it again; false if the resource has no stored image. */
	public static boolean create (Resource resource) throws IOException {
		String gsPath = gsPathOf(resource);

		if (gsPath == null) {
			return false;
		}

		ImagesService images = ImagesServiceFactory.getImagesService();
		Image original = ImagesServiceFactory
				.makeImage(read(filenameOf(gsPath)));
		Transform resize = ImagesServiceFactory.makeResize(SIZE, SIZE);
		Image scaled = images.applyTransform(resize, original,
				ImagesService.OutputEncoding.PNG);

		String thumbnailPath = gsPath + SUFFIX;
		GcsHelper.save(scaled.getImageData(), () -> "image/png",
				filenameOf(thumbnailPath));

		JsonObject properties = propertiesOf(resource);
		properties.addProperty(THUMBNAIL, "gs://" + thumbnailPath);
		properties.addProperty(THUMBNAIL_STATIC_URL, servingUrl(thumbnailPath));
		resource.properties = properties.toString();

		ResourceServiceProvider.provide().updateResource(resource);

		return true;
	}

	/** Removes the thumbnail and its mention; false if there was none. */
	public static boolean delete (Resource resource) throws IOException {
		JsonObject properties = propertiesOf(resource);

		if (!properties.has(THUMBNAIL)) {
			return false;
		}

		String stored = properties.get(THUMBNAIL).getAsString();

		if (stored.startsWith("gs://")) {
			String thumbnailPath = stored.substring("gs://".length());

			try {
				ImagesServiceFactory.getImagesService().deleteServingUrl(
						BlobstoreServiceFactory.getBlobstoreService()
								.createGsBlobKey("/gs/" + thumbnailPath));
			} catch (RuntimeException e) {
				LOG.log(Level.FINE, "No serving url to delete", e);
			}

			GcsServiceFactory.createGcsService()
					.delete(filenameOf(thumbnailPath));
		}

		properties.remove(THUMBNAIL);
		properties.remove(THUMBNAIL_STATIC_URL);
		resource.properties = properties.toString();

		ResourceServiceProvider.provide().updateResource(resource);

		return true;
	}

}

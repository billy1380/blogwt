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
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

/** Makes and removes a resource's thumbnail (`thumbnail`, `thumbnailStaticUrl` in its properties). */
public class ThumbnailHelper {

	private static final Logger LOG = Logger
			.getLogger(ThumbnailHelper.class.getName());

	/** The longest side of a thumbnail in pixels. */
	public static final int SIZE = 200;

	public static final String SUFFIX = "-thumbnail.png";
	public static final String THUMBNAIL = "thumbnail";
	public static final String THUMBNAIL_STATIC_URL = "thumbnailStaticUrl";

	/** What follows gs://: "bucket/object" or a blob key. */
	static String storedOf (Resource resource) {
		if (resource == null || resource.data == null
				|| resource.data.isEmpty()) {
			return null;
		}

		return resource.data.startsWith("gs://")
				? resource.data.substring("gs://".length())
				: resource.data;
	}

	static BlobKey keyOf (String stored) {
		return stored.contains("/")
				? BlobstoreServiceFactory.getBlobstoreService()
						.createGsBlobKey("/gs/" + stored)
				: new BlobKey(stored);
	}

	/** "bucket/object" of a stored resource, or null if it cannot be found. */
	static String gsPathOf (Resource resource) {
		String stored = storedOf(resource);

		if (stored == null) {
			return null;
		}

		if (stored.contains("/")) {
			return stored;
		}

		BlobInfo info = new BlobInfoFactory().loadBlobInfo(new BlobKey(stored));
		String gsObjectName = info == null ? null : info.getGsObjectName();

		return gsObjectName == null || gsObjectName.isEmpty() ? null
				: gsObjectName.replaceFirst("^/gs/", "");
	}

	static GcsFilename filenameOf (String gsPath) {
		int slash = gsPath.indexOf('/');

		return new GcsFilename(gsPath.substring(0, slash),
				gsPath.substring(slash + 1));
	}

	/** The resource's properties, empty if it has none, null if they are not a JSON object. */
	public static JsonObject propertiesOf (Resource resource) {
		if (resource.properties == null || resource.properties.isEmpty()) {
			return new JsonObject();
		}

		try {
			return JsonParser.parseString(resource.properties)
					.getAsJsonObject();
		} catch (JsonParseException | IllegalStateException e) {
			return null;
		}
	}

	static byte[] read (BlobKey key) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try (InputStream in = new BlobstoreInputStream(key)) {
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

	/** Whether the resource is known not to be an image; no type or application/octet-stream is tried, the images service reads the bytes. */
	public static boolean notAnImage (Resource resource) {
		JsonObject properties = propertiesOf(resource);

		if (properties == null || !properties.has("contentType")) {
			return false;
		}

		String type = properties.get("contentType").getAsString();

		return !type.isEmpty() && !type.startsWith("image/")
				&& !"application/octet-stream".equals(type);
	}

	/** Makes the thumbnail, or makes it again; false if the resource has no stored image. */
	public static boolean create (Resource resource) throws IOException {
		String stored = storedOf(resource);
		String gsPath = gsPathOf(resource);

		if (stored == null || notAnImage(resource) || gsPath == null
				|| propertiesOf(resource) == null) {
			return false;
		}

		ImagesService images = ImagesServiceFactory.getImagesService();
		Image original = ImagesServiceFactory.makeImage(read(keyOf(stored)));
		Transform resize = ImagesServiceFactory.makeResize(SIZE, SIZE);
		Image scaled = images.applyTransform(resize, original,
				ImagesService.OutputEncoding.PNG);

		String thumbnailPath = gsPath + SUFFIX;
		GcsHelper.save(scaled.getImageData(), () -> "image/png",
				filenameOf(thumbnailPath));

		return save(resource.id, properties -> {
			properties.addProperty(THUMBNAIL, "gs://" + thumbnailPath);
			properties.addProperty(THUMBNAIL_STATIC_URL,
					servingUrl(thumbnailPath));
		});
	}

	/** Changes the properties of a fresh copy, so edits made meanwhile are kept; false if they are malformed. */
	private static boolean save (Long id, Consumer<JsonObject> change) {
		Resource fresh = ResourceServiceProvider.provide().getResource(id);
		JsonObject properties = fresh == null ? null : propertiesOf(fresh);

		if (properties == null) {
			LOG.log(Level.WARNING,
					"Resource [" + id + "] is gone or its properties are not JSON");

			return false;
		}

		change.accept(properties);
		fresh.properties = properties.toString();
		ResourceServiceProvider.provide().updateResource(fresh);

		return true;
	}

	/** Removes the thumbnail and its mention; false if there was none. */
	public static boolean delete (Resource resource) throws IOException {
		JsonObject properties = propertiesOf(resource);

		if (properties == null || !properties.has(THUMBNAIL)) {
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

		return save(resource.id, fresh -> {
			fresh.remove(THUMBNAIL);
			fresh.remove(THUMBNAIL_STATIC_URL);
		});
	}

}

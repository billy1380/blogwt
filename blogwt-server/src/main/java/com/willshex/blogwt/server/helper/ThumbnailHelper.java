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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

/** Makes and removes a resource's thumbnail: the object `<id>-t.png` and `thumbnailStaticUrl` in its properties. */
public class ThumbnailHelper {

	private static final Logger LOG = Logger
			.getLogger(ThumbnailHelper.class.getName());

	/** The longest side of a thumbnail in pixels. */
	public static final int SIZE = 200;

	public static final String SUFFIX = "-t.png";
	public static final String THUMBNAIL_STATIC_URL = "thumbnailStaticUrl";

	/** Google image serving URLs only: properties can be edited, and the server fetches this. */
	private static final Pattern SERVING_URL = Pattern.compile(
			"(?:https?:)?//([a-z0-9-]+\\.(?:googleusercontent|ggpht)\\.com/[\\w/-]+)(?:=.*)?");

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

	/** The name of a resource's thumbnail object, and what the upload servlet is asked for. */
	public static String nameOf (Long id) {
		return id + SUFFIX;
	}

	/** Whether a blob-key asked of the upload servlet is a thumbnail's name. */
	public static boolean isName (String blobKey) {
		return blobKey.matches("\\d+" + SUFFIX.replace(".", "\\."));
	}

	/** "bucket/object" of a resource's thumbnail. */
	public static String pathOf (Long id) {
		return System.getProperty(GcsHelper.BUCKET_NAME_PROPERTY_KEY) + "/"
				+ nameOf(id);
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
		try (InputStream in = new BlobstoreInputStream(key)) {
			return bytesOf(in);
		}
	}

	private static byte[] bytesOf (InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[64 * 1024];
		int read;

		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}

		return out.toByteArray();
	}

	/** The resource's serving URL at thumbnail size, or null if it has none. */
	static String scaledUrl (JsonObject properties) {
		JsonElement url = properties.get("staticUrl");

		if (url == null || !url.isJsonPrimitive()) {
			return null;
		}

		Matcher serving = SERVING_URL.matcher(url.getAsString());

		return serving.matches() ? "https://" + serving.group(1) + "=s" + SIZE
				: null;
	}

	/** The image to scale: the blob or, for old uploads that cannot be read, its serving URL; null if neither. */
	private static byte[] sourceOf (Resource resource, String stored)
			throws IOException {
		try {
			return read(keyOf(stored));
		} catch (IOException e) {
			LOG.log(Level.INFO, "Cannot read the blob of resource ["
					+ resource.id + "], trying its serving url", e);

			String url = scaledUrl(propertiesOf(resource));

			return url == null ? null : fetch(url);
		}
	}

	/** What is at the url, or null if the server refuses it, as asking again would not help. */
	private static byte[] fetch (String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		int status = connection.getResponseCode();

		if (status >= 400 && status < 500) {
			return null;
		}

		try (InputStream in = connection.getInputStream()) {
			return bytesOf(in);
		}
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

	/** Makes the thumbnail, or makes it again; false if the resource has no image that can be read. */
	public static boolean create (Resource resource) throws IOException {
		String stored = storedOf(resource);

		if (stored == null || resource.id == null || notAnImage(resource)
				|| propertiesOf(resource) == null) {
			return false;
		}

		byte[] source = sourceOf(resource, stored);

		if (source == null) {
			return false;
		}

		ImagesService images = ImagesServiceFactory.getImagesService();
		Image original = ImagesServiceFactory.makeImage(source);
		Transform resize = ImagesServiceFactory.makeResize(SIZE, SIZE);
		Image scaled = images.applyTransform(resize, original,
				ImagesService.OutputEncoding.PNG);

		String thumbnailPath = pathOf(resource.id);
		GcsHelper.save(scaled.getImageData(), () -> "image/png",
				filenameOf(thumbnailPath));

		return save(resource.id, properties -> {
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

		if (properties == null || resource.id == null
				|| !properties.has(THUMBNAIL_STATIC_URL)) {
			return false;
		}

		String thumbnailPath = pathOf(resource.id);

		try {
			ImagesServiceFactory.getImagesService()
					.deleteServingUrl(BlobstoreServiceFactory
							.getBlobstoreService()
							.createGsBlobKey("/gs/" + thumbnailPath));
		} catch (RuntimeException e) {
			LOG.log(Level.FINE, "No serving url to delete", e);
		}

		GcsServiceFactory.createGcsService().delete(filenameOf(thumbnailPath));

		return save(resource.id, fresh -> fresh.remove(THUMBNAIL_STATIC_URL));
	}

}

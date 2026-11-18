package com.willshex.blogwt.server.tool;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.willshex.blogwt.server.helper.GcsHelper;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * The file behind a resource, for the export: a resource's <code>data</code> is
 * the key of a blob (with or without <code>gs://</code>), or a path in the
 * bucket.
 */
public class ResourceFiles {

	private static final Logger LOG = Logger
			.getLogger(ResourceFiles.class.getName());

	/** The most of one file that is taken. */
	public static final int MAX_BYTES = 25 * 1024 * 1024;

	/** Reads bytes <code>start</code> to <code>end</code> (inclusive) of a file. */
	public interface Fetch {
		byte[] range (long start, long end) throws Exception;
	}

	/**
	 * Joins the ranges of a file, each at most <code>chunk</code> bytes, until
	 * a short one, a missing one, or <code>max</code> bytes.
	 * 
	 * @return the file, or null when nothing could be read
	 */
	static byte[] assemble (Fetch fetch, int chunk, int max) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		long start = 0;

		while (out.size() < max) {
			byte[] part;

			try {
				part = fetch.range(start, start + chunk - 1);
			} catch (Exception e) {
				break;
			}

			if (part == null || part.length == 0) break;

			out.write(part, 0, part.length);

			if (part.length < chunk) break;

			start += chunk;
		}

		return out.size() == 0 ? null : out.toByteArray();
	}

	/** The file of the resource, or null when it has none or cannot be read. */
	public static byte[] read (Resource resource) {
		if (resource == null || resource.data == null
				|| resource.data.trim().isEmpty())
			return null;

		String key = resource.data.startsWith("gs://")
				? resource.data.substring(5) : resource.data;

		BlobstoreService blobs = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blob = new BlobKey(key);
		byte[] bytes = assemble((s, e) -> blobs.fetchData(blob, s, e),
				BlobstoreService.MAX_BLOB_FETCH_SIZE, MAX_BYTES);

		if (bytes == null) {
			try {
				byte[] stored = GcsHelper.load(key);

				bytes = stored == null || stored.length == 0 ? null : stored;
			} catch (Throwable e) {
				LOG.log(Level.FINE, "No file in the bucket for [" + key + "]", e);
			}
		}

		return bytes;
	}

}

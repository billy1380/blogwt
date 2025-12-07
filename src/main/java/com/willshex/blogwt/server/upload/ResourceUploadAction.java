//
//  ResourceUploadAction.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.upload;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.upload.Upload;

/**
 * @author William Shakour (billy1380)
 *
 */

@WebServlet(name = "Upload", urlPatterns = Upload.PATH)
public class ResourceUploadAction extends HttpServlet {

	private static final long serialVersionUID = -2428987920197775508L;
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uploadUrl = blobstoreService.createUploadUrl(Upload.PATH);
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/plain");
		resp.getWriter().print(uploadUrl);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("image");

		// StringBuffer resourcesJson = new StringBuffer();
		JsonArray resources = new JsonArray();

		if (blobKeys != null && !blobKeys.isEmpty()) {
			Resource resource;
			final Date now = new Date();

			// we only expect one really
			for (BlobKey blobKey : blobKeys) {
				resource = new Resource();
				resource.created = now;
				resource.data = "gs://"
						+ new BlobInfoFactory().loadBlobInfo(blobKey)
								.getGsObjectName().replace("/gs/", "");
				resource.description = "New uploaded file "
						+ new BlobInfoFactory().loadBlobInfo(blobKey)
								.getFilename();

				resource.name = resource.description;

				JsonObject object = new JsonObject();
				object.addProperty("contentType", new BlobInfoFactory()
						.loadBlobInfo(blobKey).getContentType());

				try {
					object.addProperty("staticUrl", ImagesServiceFactory
							.getImagesService()
							.getServingUrl(ServingUrlOptions.Builder
									.withBlobKey(blobKey))
							.replaceFirst("https:\\/\\/", "//")
							.replaceFirst("http:\\/\\/", "//"));
				} catch (Throwable e) {
				}

				resource.properties = object.toString();

				resource = ResourceServiceProvider.provide()
						.addResource(resource);

				// if (resourcesJson.length() != 0) {
				// resourcesJson.append(" ");
				// }
				//
				// resourcesJson.append(resource.id.toString());
				resources.add(resource.toJson());
			}
		}

		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		resp.getWriter().print(resources.toString());
	}

}

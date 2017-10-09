//
//  ResourceUploadAction.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.upload;

import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.JsonObject;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.CloudStorageFileItemFactory.CloudStorageFileItem;
import gwtupload.server.gae.CloudStorageUploadAction;

/**
 * @author William Shakour (billy1380)
 *
 */
@SuppressWarnings("serial")
@WebServlet(name = "Upload", urlPatterns = ResourceUploadAction.URL)
public class ResourceUploadAction extends CloudStorageUploadAction {

	public static final String URL = "/upload";

	/* (non-Javadoc)
	 * 
	 * @see
	 * gwtupload.server.gae.CloudStorageUploadAction#executeAction(javax.servlet
	 * .http.HttpServletRequest, java.util.List) */
	@Override
	public String executeAction (HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		StringBuffer resourcesJson = new StringBuffer();
		Resource resource;
		final Date now = new Date();
		for (FileItem i : sessionFiles) {
			if (!i.isFormField()) {
				resource = new Resource();
				resource.created = now;
				resource.data = "gs://"
						+ ((CloudStorageFileItem) i).getKey().getKeyString();
				resource.description = "New uploaded file " + i.getName();

				resource.name = i.getName();

				JsonObject object = new JsonObject();
				object.addProperty("contentType", i.getContentType());

				// try to get a permanent url for an image, if it works
				// add it to the description
				try {
					object.addProperty("staticUrl", ImagesServiceFactory
							.getImagesService()
							.getServingUrl(ServingUrlOptions.Builder
									.withBlobKey(((CloudStorageFileItem) i)
											.getKey()))
							.replaceFirst("https:\\/\\/", "//")
							.replaceFirst("http:\\/\\/", "//"));
				} catch (Throwable e) {}

				resource.properties = object.toString();

				resource = ResourceServiceProvider.provide()
						.addResource(resource);

				if (resourcesJson.length() != 0) {
					resourcesJson.append(" ");
				}

				resourcesJson.append(resource.id.toString());
			}
		}

		return resourcesJson.toString();
	}
}

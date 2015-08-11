//
//  ResourceUploadAction.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.upload;

import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.CloudStorageFileItemFactory.CloudStorageFileItem;
import gwtupload.server.gae.CloudStorageUploadAction;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourceUploadAction extends CloudStorageUploadAction {
	private static final long serialVersionUID = 4092355669644652816L;

	/* (non-Javadoc)
	 * 
	 * @see
	 * gwtupload.server.gae.CloudStorageUploadAction#executeAction(javax.servlet
	 * .http.HttpServletRequest, java.util.List) */
	@Override
	public String executeAction (HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
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
				resource.properties = "{\"contentType\":" + i.getContentType()
						+ "}";
				ResourceServiceProvider.provide().addResource(resource);
			}
		}

		return super.executeAction(request, sessionFiles);
	}
}

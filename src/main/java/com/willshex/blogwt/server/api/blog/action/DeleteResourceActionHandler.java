//  
//  DeleteResourceActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.ResourceValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;

public final class DeleteResourceActionHandler
		extends ActionHandler<DeleteResourceRequest, DeleteResourceResponse> {
	private static final Logger LOG = Logger
			.getLogger(DeleteResourceActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (DeleteResourceRequest input,
			DeleteResourceResponse output) throws Exception {
		ApiValidator.notNull(input, DeleteResourceRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");
		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		input.resource = ResourceValidator.lookup(input.resource,
				"input.resource");

		BlobstoreServiceFactory.getBlobstoreService()
				.delete(new BlobKey(input.resource.data.substring(5)));

		ResourceServiceProvider.provide().deleteResource(input.resource);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected DeleteResourceResponse newOutput () {
		return new DeleteResourceResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}
}
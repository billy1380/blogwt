//  
//  GetArchiveEntriesActionHandler.java
//  xsdwsdl2code
//
//  Created by William Shakour on July 20, 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.blog.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.archiveentry.ArchiveEntryServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetArchiveEntriesResponse;
import com.willshex.blogwt.shared.api.datatype.ArchiveEntry;
import com.willshex.blogwt.shared.api.datatype.Post;

public final class GetArchiveEntriesActionHandler extends
		ActionHandler<GetArchiveEntriesRequest, GetArchiveEntriesResponse> {
	private static final Logger LOG = Logger
			.getLogger(GetArchiveEntriesActionHandler.class.getName());

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.gson.web.service.server.ActionHandler#handle(com.willshex.
	 * gson.web.service.shared.Request,
	 * com.willshex.gson.web.service.shared.Response) */
	@Override
	protected void handle (GetArchiveEntriesRequest input,
			GetArchiveEntriesResponse output) throws Exception {
		ApiValidator.request(input, GetArchiveEntriesRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		output.archive = ArchiveEntryServiceProvider.provide()
				.getArchiveEntries();

		if (output.archive != null) {
			for (ArchiveEntry archiveEntry : output.archive) {
				archiveEntry.posts = PersistenceHelper.typeList(Post.class,
						archiveEntry.postKeys);
			}
		}

	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#newOutput() */
	@Override
	protected GetArchiveEntriesResponse newOutput () {
		return new GetArchiveEntriesResponse();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.ActionHandler#logger() */
	@Override
	protected Logger logger () {
		return LOG;
	}

}
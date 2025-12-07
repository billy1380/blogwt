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
	@Override
	protected void handle(GetArchiveEntriesRequest input,
			GetArchiveEntriesResponse output) throws Exception {
		ApiValidator.request(input, GetArchiveEntriesRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		if (input.session != null) {
			try {
				output.session = input.session = SessionValidator
						.lookupCheckAndExtend(input.session, "input.session");
			} catch (Exception e) {
				output.session = input.session = null;
			}
		}

		output.archive = ArchiveEntryServiceProvider.provide()
				.getArchiveEntries();

		if (output.archive != null) {
			for (ArchiveEntry archiveEntry : output.archive) {
				archiveEntry.posts = PersistenceHelper.typeList(Post.class,
						archiveEntry.postKeys);
			}
		}

	}
	@Override
	protected GetArchiveEntriesResponse newOutput() {
		return new GetArchiveEntriesResponse();
	}
	@Override
	protected Logger logger() {
		return LOG;
	}

}
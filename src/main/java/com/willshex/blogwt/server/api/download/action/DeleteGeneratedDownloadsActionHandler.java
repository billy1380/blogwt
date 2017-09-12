// 
//  DeleteGeneratedDownloadsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.download.action;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.GeneratedDownloadValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadStatusType;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.helper.DataTypeHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public final class DeleteGeneratedDownloadsActionHandler extends
		ActionHandler<DeleteGeneratedDownloadsRequest, DeleteGeneratedDownloadsResponse> {

	private static final Logger LOG = Logger
			.getLogger(DeleteGeneratedDownloadsActionHandler.class.getName());

	@Override
	public void handle (DeleteGeneratedDownloadsRequest input,
			DeleteGeneratedDownloadsResponse output) throws Exception {
		ApiValidator.notNull(input, DeleteGeneratedDownloadsRequest.class,
				"input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		PropertyValidator.ensureTrue(PropertyHelper.DOWNLOAD_ENABLED);

		input.downloads = GeneratedDownloadValidator.lookupAll(input.downloads,
				"input.downloads");

		for (GeneratedDownload download : input.downloads) {
			if (download.status == GeneratedDownloadStatusType.GeneratedDownloadStatusTypeGenerating) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.log(Level.WARNING,
							"Attempting to delete generated download ["
									+ download.id
									+ "] while it is generating. Skipping deletion.");
				}
			} else {
				if (DataTypeHelper.<User> same(download.userKey,
						input.session.user)) {
					GeneratedDownloadServiceProvider.provide()
							.deleteGeneratedDownload(download);
				} else {
					if (LOG.isLoggable(Level.INFO)) {
						LOG.log(Level.INFO,
								"Attempting to delete generated download ["
										+ download.id + "] for user with id ["
										+ download.userKey.getId()
										+ "] out of session, current session belongs to user with id ["
										+ input.session.user.id + "]");
					}
				}
			}
		}
	}

	@Override
	protected DeleteGeneratedDownloadsResponse newOutput () {
		return new DeleteGeneratedDownloadsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
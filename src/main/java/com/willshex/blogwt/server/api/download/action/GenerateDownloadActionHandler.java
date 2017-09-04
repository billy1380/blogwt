// 
//  GenerateDownloadActionHandler.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.download.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.GeneratedDownloadValidator;
import com.willshex.blogwt.server.api.validation.PropertyValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadRequest;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadResponse;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public final class GenerateDownloadActionHandler extends
		ActionHandler<GenerateDownloadRequest, GenerateDownloadResponse> {

	private static final Logger LOG = Logger
			.getLogger(GenerateDownloadActionHandler.class.getName());

	@Override
	public void handle (GenerateDownloadRequest input,
			GenerateDownloadResponse output) throws Exception {
		ApiValidator.notNull(input, GenerateDownloadRequest.class, "input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		PropertyValidator.ensureTrue(PropertyHelper.DOWNLOAD_ENABLED);
		
		if (input.download != null && input.download.user == null) {
			input.download.user = input.session.user;
		}

		input.download = GeneratedDownloadValidator.validate(input.download,
				"input.download");

		output.download = GeneratedDownloadServiceProvider.provide()
				.addGeneratedDownload(input.download);
	}

	@Override
	protected GenerateDownloadResponse newOutput () {
		return new GenerateDownloadResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
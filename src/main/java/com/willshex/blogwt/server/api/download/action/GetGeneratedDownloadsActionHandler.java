// 
//  GetGeneratedDownloadsActionHandler.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.download.action;

import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadSortType;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;

public final class GetGeneratedDownloadsActionHandler extends
		ActionHandler<GetGeneratedDownloadsRequest, GetGeneratedDownloadsResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetGeneratedDownloadsActionHandler.class.getName());

	@Override
	public void handle (GetGeneratedDownloadsRequest input,
			GetGeneratedDownloadsResponse output) throws Exception {
		ApiValidator.notNull(input, GetGeneratedDownloadsRequest.class,
				"input");
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		output.session = input.session = SessionValidator
				.lookupCheckAndExtend(input.session, "input.session");

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		}

		output.downloads = GeneratedDownloadServiceProvider.provide()
				.getUserGeneratedDownloads(input.session.user,
						input.pager.start, input.pager.count,
						GeneratedDownloadSortType.fromString(
								input.pager.sortBy),
						input.pager.sortDirection);

		output.pager = PagerHelper.moveForward(input.pager);
	}

	@Override
	protected GetGeneratedDownloadsResponse newOutput () {
		return new GetGeneratedDownloadsResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
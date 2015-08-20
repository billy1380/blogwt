//  
//  SearchApi.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.search;

import java.util.logging.Logger;

import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.blogwt.shared.api.search.call.SearchAllResponse;
import com.willshex.gson.json.service.server.ActionHandler;
import com.willshex.gson.json.service.shared.StatusType;

public final class SearchApi extends ActionHandler {
	private static final Logger LOG = Logger.getLogger(SearchApi.class.getName());

	public SearchAllResponse searchAll (SearchAllRequest input) {
		LOG.finer("Entering searchAll");
		SearchAllResponse output = new SearchAllResponse();
		try {
			output.status = StatusType.StatusTypeSuccess;
		} catch (Exception e) {
			output.status = StatusType.StatusTypeFailure;
			output.error = convertToErrorAndLog(LOG, e);
		}
		LOG.finer("Exiting searchAll");
		return output;
	}
}
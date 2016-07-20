//  
//  SearchJsonServlet.java
//  blogwt
//
//  Created by William Shakour on August 20, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.search;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.search.action.SearchAllActionHandler;
import com.willshex.blogwt.shared.api.search.call.SearchAllRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class SearchJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		if ("SearchAll".equals(action)) {
			SearchAllRequest input = new SearchAllRequest();
			input.fromJson(request);
			output = new SearchAllActionHandler().handle(input).toString();
		}
		return output;
	}
}
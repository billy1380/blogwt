//  
//  PageJsonServlet.java
//  blogwt
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page;

import com.google.gson.JsonObject;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.gson.json.service.server.JsonServlet;

@SuppressWarnings("serial")
public final class PageJsonServlet extends JsonServlet {
	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		PageApi service = new PageApi();
		if ("UpdatePage".equals(action)) {
			UpdatePageRequest input = new UpdatePageRequest();
			input.fromJson(request);
			output = service.updatePage(input).toString();
		} else if ("DeletePage".equals(action)) {
			DeletePageRequest input = new DeletePageRequest();
			input.fromJson(request);
			output = service.deletePage(input).toString();
		} else if ("CreatePage".equals(action)) {
			CreatePageRequest input = new CreatePageRequest();
			input.fromJson(request);
			output = service.createPage(input).toString();
		} else if ("GetPages".equals(action)) {
			GetPagesRequest input = new GetPagesRequest();
			input.fromJson(request);
			output = service.getPages(input).toString();
		} else if ("GetPage".equals(action)) {
			GetPageRequest input = new GetPageRequest();
			input.fromJson(request);
			output = service.getPage(input).toString();
		}
		return output;
	}
}
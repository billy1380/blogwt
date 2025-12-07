//  
//  PageJsonServlet.java
//  blogwt
//
//  Created by William Shakour on June 21, 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.page;

import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.page.action.CreatePageActionHandler;
import com.willshex.blogwt.server.api.page.action.DeletePageActionHandler;
import com.willshex.blogwt.server.api.page.action.GetPageActionHandler;
import com.willshex.blogwt.server.api.page.action.GetPagesActionHandler;
import com.willshex.blogwt.server.api.page.action.SubmitFormActionHandler;
import com.willshex.blogwt.server.api.page.action.UpdatePageActionHandler;
import com.willshex.blogwt.shared.api.page.Page;
import com.willshex.blogwt.shared.api.page.call.CreatePageRequest;
import com.willshex.blogwt.shared.api.page.call.DeletePageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPageRequest;
import com.willshex.blogwt.shared.api.page.call.GetPagesRequest;
import com.willshex.blogwt.shared.api.page.call.SubmitFormRequest;
import com.willshex.blogwt.shared.api.page.call.UpdatePageRequest;
import com.willshex.gson.web.service.server.JsonServlet;

@WebServlet(name = "Page API", urlPatterns = Page.PATH)
public final class PageJsonServlet extends JsonServlet {

	@Override
	protected String processAction(String action, JsonObject request) {
		String output = "null";
		if ("SubmitForm".equals(action)) {
			SubmitFormRequest input = new SubmitFormRequest();
			input.fromJson(request);
			output = new SubmitFormActionHandler().handle(input).toString();
		} else if ("GetPages".equals(action)) {
			GetPagesRequest input = new GetPagesRequest();
			input.fromJson(request);
			output = new GetPagesActionHandler().handle(input).toString();
		} else if ("GetPage".equals(action)) {
			GetPageRequest input = new GetPageRequest();
			input.fromJson(request);
			output = new GetPageActionHandler().handle(input).toString();
		} else if ("UpdatePage".equals(action)) {
			UpdatePageRequest input = new UpdatePageRequest();
			input.fromJson(request);
			output = new UpdatePageActionHandler().handle(input).toString();
		} else if ("DeletePage".equals(action)) {
			DeletePageRequest input = new DeletePageRequest();
			input.fromJson(request);
			output = new DeletePageActionHandler().handle(input).toString();
		} else if ("CreatePage".equals(action)) {
			CreatePageRequest input = new CreatePageRequest();
			input.fromJson(request);
			output = new CreatePageActionHandler().handle(input).toString();
		}
		return output;
	}
}
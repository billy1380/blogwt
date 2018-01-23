// 
//  DownloadJsonServlet.java
//  blogwt
// 
//  Created by William Shakour on June 26, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.download;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.download.action.DeleteGeneratedDownloadsActionHandler;
import com.willshex.blogwt.server.api.download.action.GenerateDownloadActionHandler;
import com.willshex.blogwt.server.api.download.action.GetGeneratedDownloadsActionHandler;
import com.willshex.blogwt.server.background.generatedownload.generator.DownloadGeneratorProvider;
import com.willshex.blogwt.server.helper.GcsHelper;
import com.willshex.blogwt.server.helper.GeneratedDownloadHelper;
import com.willshex.blogwt.server.helper.ServletHelper;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.Session;
import com.willshex.blogwt.shared.api.download.Download;
import com.willshex.blogwt.shared.api.download.call.DeleteGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.api.download.call.GenerateDownloadRequest;
import com.willshex.blogwt.shared.api.download.call.GetGeneratedDownloadsRequest;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.blogwt.shared.page.search.Filter;
import com.willshex.gson.web.service.server.JsonServlet;
import com.willshex.utility.StringUtils;

@SuppressWarnings("serial")
@WebServlet(name = "Download API", urlPatterns = Download.PATH)
public final class DownloadJsonServlet extends JsonServlet {

	private static final String ENTITY_ID_KEY = "id";

	@Override
	protected String processAction (String action, JsonObject request) {
		String output = "null";
		if ("DeleteGeneratedDownloads".equals(action)) {
			DeleteGeneratedDownloadsRequest input = new DeleteGeneratedDownloadsRequest();
			input.fromJson(request);
			output = new DeleteGeneratedDownloadsActionHandler().handle(input)
					.toString();
		} else if ("GenerateDownload".equals(action)) {
			GenerateDownloadRequest input = new GenerateDownloadRequest();
			input.fromJson(request);
			output = new GenerateDownloadActionHandler().handle(input)
					.toString();
		} else if ("GetGeneratedDownloads".equals(action)) {
			GetGeneratedDownloadsRequest input = new GetGeneratedDownloadsRequest();
			input.fromJson(request);
			output = new GetGeneratedDownloadsActionHandler().handle(input)
					.toString();
		}
		return output;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.gson.web.service.server.JsonServlet#doGet() */
	@Override
	protected void doGet () throws IOException {
		String action = REQUEST.get().getParameter("action");
		String request = REQUEST.get().getParameter("request");

		if (request == null && "download".equals(action)) {
			Session userSession = ServletHelper.session(REQUEST.get());

			if (userSession != null) {
				String idParam = REQUEST.get().getParameter(ENTITY_ID_KEY);

				if (idParam != null) {
					Long id = Long.valueOf(idParam);

					GeneratedDownload generatedDownload = GeneratedDownloadServiceProvider
							.provide().getGeneratedDownload(id);

					if (generatedDownload.userKey.getId() == userSession.userKey
							.getId()) {
						Filter filter = Filter.fromStack(
								Stack.parse(generatedDownload.parameters));

						String fileName = StringUtils
								.urldecode(generatedDownload.parameters)
								.replace(Filter.QUERY + "/", "")
								.replace("/", "_").replace("&", "_")
								.replace(" ", "_") + "."
								+ DownloadGeneratorProvider
										.extension(filter.type).get();

						RESPONSE.get().setContentType(DownloadGeneratorProvider
								.contentType(filter.type).get());
						RESPONSE.get().setHeader("content-disposition",
								"inline; filename=\"" + fileName + "\"");
						RESPONSE.get().getOutputStream()
								.write(GcsHelper.load(GeneratedDownloadHelper
										.path(generatedDownload, filter)));
					} else {
						RESPONSE.get().sendError(403,
								"Access denied, cannot download other user's file");
					}
				}
			} else {
				RESPONSE.get().sendError(403,
						"Access denied, cannot download file without logging in");
			}
		} else {
			super.doGet();
		}
	}
}

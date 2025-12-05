//
//  GenerateDownloadServlet.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.background.generatedownload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonObject;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.GeneratedDownloadValidator;
import com.willshex.blogwt.server.background.generatedownload.generator.DownloadGeneratorProvider;
import com.willshex.blogwt.server.background.generatedownload.generator.IGenerator;
import com.willshex.blogwt.server.background.generatedownload.input.GenerateDownloadAction;
import com.willshex.blogwt.server.helper.GeneratedDownloadHelper;
import com.willshex.blogwt.server.helper.QueueHelper;
import com.willshex.blogwt.server.helper.QueueHelper.HasQueueAction;
import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownloadStatusType;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.page.Stack;
import com.willshex.blogwt.shared.page.search.Filter;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.server.ContextAwareServlet;

/**
 * @author William Shakour (billy1380)
 *
 */

@WebServlet(name = "Generate Download", urlPatterns = GenerateDownloadServlet.URL)
public class GenerateDownloadServlet extends ContextAwareServlet
		implements HasQueueAction {

	private static final String GENERATE_DOWNLOAD_ACTION = "Generate";

	public static final String URL = "/generatedownload";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.server.ContextAwareServlet#doGet()
	 */
	@Override
	protected void doGet() throws ServletException, IOException {
		super.doGet();

		try {
			QueueHelper.processGet(this);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.server.ContextAwareServlet#doPost()
	 */
	@Override
	protected void doPost() throws ServletException, IOException {
		doGet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.helper.QueueHelper.HasQueueAction#
	 * processAction(java.lang.String, com.google.gson.JsonObject)
	 */
	@Override
	public void processAction(String action, JsonObject json)
			throws ServiceException {
		if (GENERATE_DOWNLOAD_ACTION.equals(action)) {
			GenerateDownloadAction input = new GenerateDownloadAction();
			input.fromJson(json);
			processGenerateDownload(input);
		}
	}

	/**
	 * @param input
	 * @throws InputValidationException
	 */
	private void processGenerateDownload(GenerateDownloadAction input)
			throws ServiceException {
		GeneratedDownload generatedDownload = GeneratedDownloadValidator
				.lookup(input.download, "input.download");

		try {
			generatedDownload.status = GeneratedDownloadStatusType.GeneratedDownloadStatusTypeGenerating;
			GeneratedDownloadServiceProvider.provide()
					.updateGeneratedDownload(generatedDownload);

			Stack stack = Stack.parse(generatedDownload.parameters);
			Filter filter = Filter.fromStack(stack);

			IGenerator generator = DownloadGeneratorProvider.generator(filter.type);

			if (generator == null)
				ApiValidator.throwServiceError(ServiceException.class,
						ApiError.NoGeneratorFound, filter.type);

			byte[] bytes = generator.generate(generatedDownload, filter);

			if (bytes != null
					&& generatedDownload.parameters.endsWith("send")) {
				GeneratedDownloadHelper.sendEmail(generatedDownload, filter,
						bytes);
			}

			generatedDownload.status = GeneratedDownloadStatusType.GeneratedDownloadStatusTypeReady;
			generatedDownload.url = "/download?action=download&id="
					+ generatedDownload.id;

			GeneratedDownloadServiceProvider.provide()
					.updateGeneratedDownload(generatedDownload);
		} catch (Throwable t) {
			generatedDownload.status = GeneratedDownloadStatusType.GeneratedDownloadStatusTypeError;
			GeneratedDownloadServiceProvider.provide()
					.updateGeneratedDownload(generatedDownload);

			throw new RuntimeException(t);
		}
	}

	public static void generateDownload(GeneratedDownload download) {
		QueueHelper.enqueue(URL, GENERATE_DOWNLOAD_ACTION,
				new GenerateDownloadAction()
						.download((GeneratedDownload) new GeneratedDownload()
								.id(download.id)));
	}

}

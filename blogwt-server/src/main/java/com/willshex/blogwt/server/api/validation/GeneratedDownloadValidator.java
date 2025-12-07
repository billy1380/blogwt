//
//  GeneratedDownloadValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 26 Jun 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.generateddownload.GeneratedDownloadServiceProvider;
import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class GeneratedDownloadValidator extends ApiValidator {
	private static final Class<GeneratedDownload> CLASS = GeneratedDownload.class;
	private static final String TYPE = CLASS.getSimpleName();

	private static final Processor<GeneratedDownload> LOOKUP = new Processor<GeneratedDownload>() {

		@Override
		public GeneratedDownload process (GeneratedDownload item, String name)
				throws InputValidationException {
			return lookup(item, name);
		}
	};

	public static GeneratedDownload validate (
			GeneratedDownload generatedDownload, String name)
			throws InputValidationException {
		if (generatedDownload == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name);

		return generatedDownload;
	}

	public static GeneratedDownload lookup (GeneratedDownload generatedDownload,
			String name) throws InputValidationException {
		if (generatedDownload == null)
			throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name);

		notNull(generatedDownload.id, Long.class, name + ".id");

		GeneratedDownload lookupGeneratedDownload = GeneratedDownloadServiceProvider
				.provide().getGeneratedDownload(generatedDownload.id);

		if (lookupGeneratedDownload == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupGeneratedDownload;
	}

	public static <T extends Iterable<GeneratedDownload>> T lookupAll (
			T generatedDownload, String name) throws ServiceException {
		return processAll(false, generatedDownload, LOOKUP, TYPE, name);
	}
}

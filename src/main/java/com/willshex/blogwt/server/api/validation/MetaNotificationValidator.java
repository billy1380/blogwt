//
//  MetaNotificationValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.metanotification.MetaNotificationServiceProvider;
import com.willshex.blogwt.shared.api.datatype.MetaNotification;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class MetaNotificationValidator extends ApiValidator {
	private static final Class<MetaNotification> CLASS = MetaNotification.class;
	private static final String TYPE = CLASS.getSimpleName();

	public static MetaNotification lookup (MetaNotification metaNotification,
			String name) throws InputValidationException {
		notNull(metaNotification, CLASS, name);

		boolean isIdLookup = false, isCodeLookup = false;

		if (metaNotification.id != null) {
			isIdLookup = true;
		} else if (metaNotification.code != null) {
			isCodeLookup = true;
		}

		MetaNotification lookupMetaNotification = null;
		if (isIdLookup) {
			metaNotification = MetaNotificationServiceProvider.provide()
					.getMetaNotification(metaNotification.id);
		} else if (isCodeLookup) {
			metaNotification = MetaNotificationServiceProvider.provide()
					.getCodeMetaNotification(metaNotification.code);
		}

		if (lookupMetaNotification == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return metaNotification;
	}
}

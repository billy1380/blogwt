//
//  NotificationSettingValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.notificationsetting.NotificationSettingServiceProvider;
import com.willshex.blogwt.shared.api.datatype.NotificationSetting;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class NotificationSettingValidator extends ApiValidator {
	public static final Class<NotificationSetting> CLASS = NotificationSetting.class;
	public static final String TYPE = CLASS.getSimpleName();

	public static NotificationSetting lookup (
			NotificationSetting notificationSetting, String name)
			throws InputValidationException {
		notNull(notificationSetting, CLASS, name);

		boolean isIdLookup = false, isUserMetaNotificationLookup = false;

		if (notificationSetting.id != null) {
			isIdLookup = true;
		} else if (notificationSetting.user != null
				&& notificationSetting.meta != null) {
			isUserMetaNotificationLookup = true;
		}

		if (!(isIdLookup || isUserMetaNotificationLookup))
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, TYPE + ": " + name);

		NotificationSetting lookupNotificationSetting = null;
		if (isIdLookup) {
			notificationSetting = NotificationSettingServiceProvider.provide()
					.getNotificationSetting(notificationSetting.id);
		} else if (isUserMetaNotificationLookup) {
			notificationSetting.user = UserValidator
					.lookup(notificationSetting.user, name + ".user");

			notificationSetting.meta = MetaNotificationValidator
					.lookup(notificationSetting.meta, name + ".meta");

			notificationSetting = NotificationSettingServiceProvider.provide()
					.getMetaUserNotificationSetting(notificationSetting.meta,
							notificationSetting.user);
		}

		if (lookupNotificationSetting == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return notificationSetting;
	}

	public static NotificationSetting validate (
			NotificationSetting notificationSetting, String name)
			throws InputValidationException {
		notNull(notificationSetting, CLASS, name);

		return notificationSetting;
	}

	public static <T extends Iterable<NotificationSetting>> T validateAll (
			T notificationSettings, String name) throws ServiceException {
		return processAll(false, notificationSettings,
				NotificationSettingValidator::validate, TYPE, name);
	}

	public static <T extends Iterable<NotificationSetting>> T lookupAll (
			T notificationSettings, String name) throws ServiceException {
		return processAll(false, notificationSettings,
				NotificationSettingValidator::lookup, TYPE, name);
	}
}

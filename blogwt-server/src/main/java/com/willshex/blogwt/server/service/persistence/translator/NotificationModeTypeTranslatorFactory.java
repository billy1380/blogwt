//
//  NotificationModeTypeTranslatorFactory.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.persistence.translator;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.CreateContext;
import com.googlecode.objectify.impl.translate.LoadContext;
import com.googlecode.objectify.impl.translate.SaveContext;
import com.googlecode.objectify.impl.translate.SkipException;
import com.googlecode.objectify.impl.translate.TypeKey;
import com.googlecode.objectify.impl.translate.ValueTranslator;
import com.googlecode.objectify.impl.translate.ValueTranslatorFactory;
import com.willshex.blogwt.shared.api.datatype.NotificationModeType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class NotificationModeTypeTranslatorFactory
		extends ValueTranslatorFactory<NotificationModeType, String> {

	public NotificationModeTypeTranslatorFactory () {
		super(NotificationModeType.class);
	}

	@Override
	protected ValueTranslator<NotificationModeType, String> createValueTranslator (
			TypeKey<NotificationModeType> tk, CreateContext ctx, Path path) {
		return new ValueTranslator<NotificationModeType, String>(String.class) {
			@Override
			protected String saveValue (NotificationModeType pojo,
					boolean index, SaveContext ctx, Path path)
					throws SkipException {
				return pojo.toString();
			}

			@Override
			protected NotificationModeType loadValue (String node,
					LoadContext ctx, Path path) throws SkipException {
				NotificationModeType value = null;

				if (node != null && node.length() > 0) {
					value = NotificationModeType.fromString(node);

					if (value == null) {
						try {
							value = Enum.valueOf(NotificationModeType.class,
									node);
						} catch (IllegalArgumentException
								| NullPointerException ex) {}
					}
				}

				return value;
			}
		};
	}
}

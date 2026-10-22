//
//  NotificationModeTypeTranslatorFactory.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 10 May 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.persistence.translator;

import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueType;
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
		return new ValueTranslator<NotificationModeType, String>(ValueType.STRING) {
			@Override
			protected Value<String> saveValue (NotificationModeType pojo,
					SaveContext ctx, Path path)
					throws SkipException {
				return StringValue.newBuilder(pojo.toString()).build();
			}

			@Override
			protected NotificationModeType loadValue (Value<String> node,
					LoadContext ctx, Path path) throws SkipException {
				NotificationModeType value = null;

				if (node.get() != null && node.get().length() > 0) {
					value = NotificationModeType.fromString(node.get());

					if (value == null) {
						try {
							value = Enum.valueOf(NotificationModeType.class,
									node.get());
						} catch (IllegalArgumentException
								| NullPointerException ex) {}
					}
				}

				return value;
			}
		};
	}
}

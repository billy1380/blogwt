//
//  PermissionTypeTypeTranslatorFactory.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 23 Jul 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
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
import com.willshex.blogwt.shared.api.datatype.PermissionTypeType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PermissionTypeTypeTranslatorFactory
		extends ValueTranslatorFactory<PermissionTypeType, String> {

	public PermissionTypeTypeTranslatorFactory () {
		super(PermissionTypeType.class);
	}

	@Override
	protected ValueTranslator<PermissionTypeType, String> createValueTranslator (
			TypeKey<PermissionTypeType> tk, CreateContext ctx, Path path) {
		return new ValueTranslator<PermissionTypeType, String>(ValueType.STRING) {
			@Override
			protected Value<String> saveValue (PermissionTypeType pojo,
					SaveContext ctx, Path path) throws SkipException {
				return StringValue.newBuilder(pojo.toString()).build();
			}

			@Override
			protected PermissionTypeType loadValue (Value<String> node,
					LoadContext ctx, Path path) throws SkipException {
				PermissionTypeType value = null;

				if (node.get() != null && node.get().length() > 0) {
					value = PermissionTypeType.fromString(node.get());

					if (value == null) {
						try {
							value = Enum.valueOf(PermissionTypeType.class,
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
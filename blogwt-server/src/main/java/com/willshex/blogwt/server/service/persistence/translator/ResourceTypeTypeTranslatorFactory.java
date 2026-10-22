//
//  ResourceTypeTypeTranslatorFactory.java
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
import com.willshex.blogwt.shared.api.datatype.ResourceTypeType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourceTypeTypeTranslatorFactory
		extends ValueTranslatorFactory<ResourceTypeType, String> {

	public ResourceTypeTypeTranslatorFactory () {
		super(ResourceTypeType.class);
	}

	@Override
	protected ValueTranslator<ResourceTypeType, String> createValueTranslator (
			TypeKey<ResourceTypeType> tk, CreateContext ctx, Path path) {
		return new ValueTranslator<ResourceTypeType, String>(ValueType.STRING) {
			@Override
			protected Value<String> saveValue (ResourceTypeType pojo,
					SaveContext ctx, Path path) throws SkipException {
				return StringValue.newBuilder(pojo.toString()).build();
			}

			@Override
			protected ResourceTypeType loadValue (Value<String> node, LoadContext ctx,
					Path path) throws SkipException {
				ResourceTypeType value = null;

				if (node.get() != null && node.get().length() > 0) {
					value = ResourceTypeType.fromString(node.get());

					if (value == null) {
						try {
							value = Enum.valueOf(ResourceTypeType.class, node.get());
						} catch (IllegalArgumentException
								| NullPointerException ex) {}
					}
				}

				return value;
			}
		};
	}
}
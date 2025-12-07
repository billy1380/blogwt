//
//  ResourceTypeTypeTranslatorFactory.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 23 Jul 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
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
		return new ValueTranslator<ResourceTypeType, String>(String.class) {
			@Override
			protected String saveValue (ResourceTypeType pojo, boolean index,
					SaveContext ctx, Path path) throws SkipException {
				return pojo.toString();
			}

			@Override
			protected ResourceTypeType loadValue (String node, LoadContext ctx,
					Path path) throws SkipException {
				ResourceTypeType value = null;

				if (node != null && node.length() > 0) {
					value = ResourceTypeType.fromString(node);

					if (value == null) {
						try {
							value = Enum.valueOf(ResourceTypeType.class, node);
						} catch (IllegalArgumentException
								| NullPointerException ex) {}
					}
				}

				return value;
			}
		};
	}
}
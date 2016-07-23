//
//  PermissionTypeTypeTranslatorFactory.java
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
		return new ValueTranslator<PermissionTypeType, String>(String.class) {

			/* (non-Javadoc)
			 * 
			 * @see
			 * com.googlecode.objectify.impl.translate.ValueTranslator#saveValue
			 * (java.lang.Object, boolean,
			 * com.googlecode.objectify.impl.translate.SaveContext,
			 * com.googlecode.objectify.impl.Path) */
			@Override
			protected String saveValue (PermissionTypeType pojo, boolean index,
					SaveContext ctx, Path path) throws SkipException {
				return pojo.toString();
			}

			@Override
			protected PermissionTypeType loadValue (String node,
					LoadContext ctx, Path path) throws SkipException {
				PermissionTypeType value = null;

				if (node != null && node.length() > 0) {
					value = PermissionTypeType.fromString(node);

					if (value == null) {
						try {
							value = Enum.valueOf(PermissionTypeType.class,
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
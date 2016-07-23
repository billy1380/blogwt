//
//  RelationshipTypeTypeTranslatorFactory.java
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
import com.willshex.blogwt.shared.api.datatype.RelationshipTypeType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class RelationshipTypeTypeTranslatorFactory
		extends ValueTranslatorFactory<RelationshipTypeType, String> {

	public RelationshipTypeTypeTranslatorFactory () {
		super(RelationshipTypeType.class);
	}

	@Override
	protected ValueTranslator<RelationshipTypeType, String> createValueTranslator (
			TypeKey<RelationshipTypeType> tk, CreateContext ctx, Path path) {
		return new ValueTranslator<RelationshipTypeType, String>(String.class) {

			/* (non-Javadoc)
			 * 
			 * @see
			 * com.googlecode.objectify.impl.translate.ValueTranslator#saveValue
			 * (java.lang.Object, boolean,
			 * com.googlecode.objectify.impl.translate.SaveContext,
			 * com.googlecode.objectify.impl.Path) */
			@Override
			protected String saveValue (RelationshipTypeType pojo,
					boolean index, SaveContext ctx, Path path)
					throws SkipException {
				return pojo.toString();
			}

			@Override
			protected RelationshipTypeType loadValue (String node,
					LoadContext ctx, Path path) throws SkipException {
				RelationshipTypeType value = null;

				if (node != null && node.length() > 0) {
					value = RelationshipTypeType.fromString(node);

					if (value == null) {
						try {
							value = Enum.valueOf(RelationshipTypeType.class,
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
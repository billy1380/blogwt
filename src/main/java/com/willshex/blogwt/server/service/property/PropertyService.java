//  
//  PropertyService.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.property;

import static com.willshex.blogwt.server.helper.PersistenceHelper.keyToId;
import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.Property;

final class PropertyService implements IPropertyService {
	public String getName () {
		return NAME;
	}

	public Property getProperty (Long id) {
		return provide().load().type(Property.class).id(id.longValue()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.property.IPropertyService
	 * #addProperty(com.willshex.blogwt.shared.api.datatypes.Property ) */
	@Override
	public Property addProperty (Property property) {
		if (property.created == null) {
			property.created = new Date();
		}

		Key<Property> key = provide().save().entity(property).now();
		property.id = keyToId(key);
		return property;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.property.IPropertyService
	 * #updateProperty(com.willshex.blogwt.shared.api.datatypes .Property) */
	@Override
	public Property updateProperty (Property property) {
		provide().save().entity(property).now();
		return property;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.property.IPropertyService
	 * #deleteProperty(com.willshex.blogwt.shared.api.datatypes .Property) */
	@Override
	public void deleteProperty (Property property) {
		provide().delete().entity(property).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.services.property.IPropertyService
	 * #getNamedProperty(java.lang.String) */
	@Override
	public Property getNamedProperty (String name) {
		return provide().load().type(Property.class).filter("name", name)
				.first().now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.property.IPropertyService#
	 * addPropertyBatch (java.util.Collection) */
	@Override
	public void addPropertyBatch (Collection<Property> properties) {
		for (Property property : properties) {
			if (property.created == null) {
				property.created = new Date();
			}
		}

		provide().save().entities(properties).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.property.IPropertyService#
	 * getProperties () */
	@Override
	public List<Property> getProperties () {
		return provide().load().type(Property.class).list();
	}

}
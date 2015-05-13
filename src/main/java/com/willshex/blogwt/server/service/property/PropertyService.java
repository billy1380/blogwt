//  
//  PropertyService.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.property;

import java.util.Date;

import static com.willshex.blogwt.server.service.PersistenceService.ofy;

import com.googlecode.objectify.Key;
import com.willshex.blogwt.shared.api.datatype.Property;

final class PropertyService implements IPropertyService {
	public String getName () {
		return NAME;
	}

	public Property getProperty (Long id) {
		return ofy().load().type(Property.class).id(id.longValue()).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.property.IPropertyService
	 * #addProperty(com.spacehopperstudios.blogwt.shared.api.datatypes.Property
	 * ) */
	@Override
	public Property addProperty (Property property) {
		if (property.created == null) {
			property.created = new Date();
		}

		Key<Property> key = ofy().save().entity(property).now();
		property.id = Long.valueOf(key.getId());
		return property;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.property.IPropertyService
	 * #updateProperty(com.spacehopperstudios.blogwt.shared.api.datatypes
	 * .Property) */
	@Override
	public Property updateProperty (Property property) {
		ofy().save().entity(property).now();
		return property;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.property.IPropertyService
	 * #deleteProperty(com.spacehopperstudios.blogwt.shared.api.datatypes
	 * .Property) */
	@Override
	public void deleteProperty (Property property) {
		ofy().delete().entity(property).now();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.blogwt.server.services.property.IPropertyService
	 * #getNamedProperty(java.lang.String) */
	@Override
	public Property getNamedProperty (String name) {
		return ofy().load().type(Property.class).filter("name", name).first()
				.now();
	}

}
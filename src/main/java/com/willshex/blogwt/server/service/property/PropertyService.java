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
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.server.helper.PersistenceHelper;
import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.PropertySortType;

final class PropertyService
		implements IPropertyService, ISortable<PropertySortType> {
	public String getName () {
		return NAME;
	}

	public Property getProperty (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Property> load () {
		return provide().load().type(Property.class);
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
		return PersistenceHelper.one(load().filter("name", name));
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
	 * getProperties(java.lang.Integer, java.lang.Integer,
	 * com.willshex.blogwt.shared.api.datatype.PropertySortType,
	 * com.willshex.blogwt.shared.api.SortDirectionType) */
	@Override
	public List<Property> getProperties (Integer start, Integer count,
			PropertySortType sortBy, SortDirectionType sortDirection) {
		return PersistenceHelper.pagedAndSorted(load(), start, count, sortBy,
				this, sortDirection).list();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.ISortable#map(java.lang.Enum) */
	@Override
	public String map (PropertySortType sortBy) {
		return sortBy.toString();
	}

}
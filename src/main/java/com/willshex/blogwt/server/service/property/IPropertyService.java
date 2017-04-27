//  
//  IPropertyService.java
//  blogwt
//
//  Created by William Shakour on May 13, 2015.
//  Copyrights © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.property;

import java.util.Collection;
import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.PropertySortType;
import com.willshex.service.IService;

public interface IPropertyService extends IService {

	public static final String NAME = "blogwt.property";

	/**
	 * @param id
	 * @return
	 */
	public Property getProperty (Long id);

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Property getNamedProperty (String name);

	/**
	 * @param property
	 * @return
	 */
	public Property addProperty (Property property);

	/**
	 * @param property
	 * @return
	 */
	public Property updateProperty (Property property);

	/**
	 * @param property
	 */
	public void deleteProperty (Property property);

	/**
	 * 
	 * @param properties
	 */
	public void addPropertyBatch (Collection<Property> properties);

	/**
	 * Get Properties
	 * @param start
	 * @param count
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public List<Property> getProperties (Integer start, Integer count,
			PropertySortType sortBy, SortDirectionType sortDirection);

}
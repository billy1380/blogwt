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

import com.spacehopperstudios.service.IService;
import com.willshex.blogwt.shared.api.datatype.Property;

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
	 * @return
	 */
	public List<Property> getProperties ();

}
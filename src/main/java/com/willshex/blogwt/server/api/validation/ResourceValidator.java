//
//  ResourceValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.List;

import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourceValidator {

	private static final String type = Resource.class.getSimpleName();

	public static Resource validate (Resource resource, String name)
			throws InputValidationException {
		if (resource == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		return resource;
	}

	public static Resource lookup (Resource resource, String name)
			throws InputValidationException {
		if (resource == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false;

		if (resource.id != null) {
			isIdLookup = true;
		}

		if (!isIdLookup)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Resource lookupResource = null;
		if (isIdLookup) {
			lookupResource = ResourceServiceProvider.provide()
					.getResource(resource.id);
		}

		if (lookupResource == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupResource;
	}

	/**
	 * @param resources
	 * @return 
	 * @throws InputValidationException 
	 */
	public static List<Resource> lookupAll (List<Resource> resources,
			String name) throws InputValidationException {
		if (resources == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		List<Resource> lookupResources = new ArrayList<Resource>();

		for (Resource resource : resources) {
			lookupResources.add(lookup(resource, name + "[n]"));
		}

		return lookupResources;
	}

}

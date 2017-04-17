//
//  ResourceValidator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 15 Aug 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.validation;

import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.InputValidationException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourceValidator extends ApiValidator {
	private static final String TYPE = Resource.class.getSimpleName();
	private static final Processor<Resource> LOOKUP = new Processor<Resource>() {

		@Override
		public Resource process (Resource item, String name)
				throws InputValidationException {
			return lookup(item, name);
		}
	};

	public static Resource validate (Resource resource, String name)
			throws InputValidationException {
		if (resource == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		return resource;
	}

	public static Resource lookup (Resource resource, String name)
			throws InputValidationException {
		if (resource == null) throwServiceError(InputValidationException.class,
				ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false;

		if (resource.id != null) {
			isIdLookup = true;
		}

		if (!isIdLookup) throwServiceError(InputValidationException.class,
				ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Resource lookupResource = null;
		if (isIdLookup) {
			lookupResource = ResourceServiceProvider.provide()
					.getResource(resource.id);
		}

		if (lookupResource == null)
			throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupResource;
	}

	/**
	 * @param resources
	 * @return 
	 * @throws InputValidationException 
	 */
	public static <T extends Iterable<Resource>> T lookupAll (T resources,
			String name) throws InputValidationException {
		return processAll(false, resources, LOOKUP, TYPE, name);
	}

}

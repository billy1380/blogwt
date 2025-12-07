//
//  DisallowedByPropertyException.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.api.exception;

import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.gson.web.service.server.ServiceException;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DisallowedByPropertyException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2612695784130953220L;

	/**
	 * 
	 */
	public DisallowedByPropertyException (Property property) {
		super(ApiError.PropertyDisallowed.getCode(),
				ApiError.PropertyDisallowed.getMessage(property == null
						|| property.name == null ? "unknown" : (property.name
						+ " = " + property.value)));

	}

	/**
	 * 
	 */
	public DisallowedByPropertyException (String propertyName) {
		super(ApiError.PropertyDisallowed.getCode(),
				ApiError.PropertyDisallowed
						.getMessage(propertyName == null ? "unknown"
								: propertyName));
	}
}

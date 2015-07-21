package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.spacehopperstudios.utility.StringUtils;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.json.service.server.InputValidationException;
import com.willshex.gson.json.service.server.ServiceException;

public class PropertyValidator {
	private static final String type = Property.class.getSimpleName();

	public static List<Property> setup (Collection<Property> properties,
			String name) throws ServiceException {
		if (properties == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + "[]: " + name);

		HashSet<String> notFound = new HashSet<String>();

		notFound.add(PropertyHelper.TITLE);
		notFound.add(PropertyHelper.EXTENDED_TITLE);
		notFound.add(PropertyHelper.COPYRIGHT_HOLDER);
		notFound.add(PropertyHelper.COPYRIGHT_URL);

		List<Property> setupProperties = new ArrayList<Property>();

		for (Property property : properties) {
			switch (property.name) {
			case PropertyHelper.TITLE:
			case PropertyHelper.EXTENDED_TITLE:
			case PropertyHelper.COPYRIGHT_HOLDER:
			case PropertyHelper.COPYRIGHT_URL:
				notFound.remove(property.name);
				setupProperties.add(property);
				break;
			}
		}

		if (notFound.size() != 0)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.MissingProperties,
					name + "(" + StringUtils.join(notFound) + ")");

		validateAll(setupProperties, name);

		return setupProperties;
	}

	public static <T extends Iterable<Property>> T validateAll (T properties,
			String name) throws InputValidationException {
		for (Property property : properties) {
			validate(property, name + "[n]");
		}
		return properties;
	}

	public static Property validate (Property property, String name)
			throws InputValidationException {
		if (property == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		return property;
	}

	public static Property lookup (Property property, String name)
			throws InputValidationException {
		if (property == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, type + ": " + name);

		boolean isIdLookup = false, isNameLookup = false;
		if (property.id != null) {
			isIdLookup = true;
		} else if (property.name != null) {
			isNameLookup = true;
		}

		if (!(isIdLookup || isNameLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, type + ": " + name);

		Property lookupProperty;

		if (isIdLookup) {
			lookupProperty = PropertyServiceProvider.provide().getProperty(property.id);
		} else {
			lookupProperty = PropertyServiceProvider.provide().getNamedProperty(property.name);
		}

		if (lookupProperty == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, type + ": " + name);

		return lookupProperty;
	}
}

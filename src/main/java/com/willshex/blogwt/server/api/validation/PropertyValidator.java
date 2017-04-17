package com.willshex.blogwt.server.api.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.willshex.blogwt.server.api.exception.DisallowedByPropertyException;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.validation.ApiError;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.server.InputValidationException;
import com.willshex.gson.web.service.server.ServiceException;
import com.willshex.utility.StringUtils;

public class PropertyValidator extends ApiValidator {
	private static final String TYPE = Property.class.getSimpleName();
	private static final Processor<Property> VALIDATE = new Processor<Property>() {

		@Override
		public Property process (Property item, String name)
				throws InputValidationException {
			return validate(item, name);
		}
	};

	public static List<Property> setup (Collection<Property> properties,
			String name) throws ServiceException {
		if (properties == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + "[]: " + name);

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
		return processAll(true, properties, VALIDATE, TYPE, name);
	}

	public static Property validate (Property property, String name)
			throws InputValidationException {
		if (property == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name);

		return property;
	}

	public static Property lookup (Property property, String name)
			throws InputValidationException {
		if (property == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.InvalidValueNull, TYPE + ": " + name);

		boolean isIdLookup = false, isNameLookup = false;
		if (property.id != null) {
			isIdLookup = true;
		} else if (property.name != null) {
			isNameLookup = true;
		}

		if (!(isIdLookup || isNameLookup))
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNoLookup, TYPE + ": " + name);

		Property lookupProperty;

		if (isIdLookup) {
			lookupProperty = PropertyServiceProvider.provide()
					.getProperty(property.id);
		} else {
			lookupProperty = PropertyServiceProvider.provide()
					.getNamedProperty(property.name);
		}

		if (lookupProperty == null)
			ApiValidator.throwServiceError(InputValidationException.class,
					ApiError.DataTypeNotFound, TYPE + ": " + name);

		return lookupProperty;
	}

	/**
	 * Checks whether the property or properties are all true if not throws an exception
	 * @param propertyName
	 * @throws DisallowedByPropertyException
	 */
	public static void ensureTrue (String... propertyName)
			throws DisallowedByPropertyException {
		for (String name : propertyName) {
			Property property = PropertyServiceProvider.provide()
					.getNamedProperty(name);

			if (PropertyHelper.isEmpty(property))
				throw new DisallowedByPropertyException(name);

			if (Boolean.FALSE.equals(Boolean.valueOf(property.value)))
				throw new DisallowedByPropertyException(property);
		}
	}
}

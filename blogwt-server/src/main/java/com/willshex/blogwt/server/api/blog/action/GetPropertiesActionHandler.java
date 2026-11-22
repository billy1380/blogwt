// 
//  GetPropertiesActionHandler.java
//  blogwt
// 
//  Created by William Shakour on April 27, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.blog.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.api.blog.BlogJsonServlet;
import com.willshex.blogwt.server.api.validation.ApiValidator;
import com.willshex.blogwt.server.api.validation.SessionValidator;
import com.willshex.blogwt.server.api.validation.UserValidator;
import com.willshex.blogwt.server.service.property.PropertyServiceProvider;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.PropertySortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;

public final class GetPropertiesActionHandler
		extends ActionHandler<GetPropertiesRequest, GetPropertiesResponse> {

	private static final Logger LOG = Logger
			.getLogger(GetPropertiesActionHandler.class.getName());

	@Override
	public void handle (GetPropertiesRequest input,
			GetPropertiesResponse output) throws Exception {
		ApiValidator.request(input, GetPropertiesRequest.class);
		ApiValidator.accessCode(input.accessCode, "input.accessCode");

		try {
			output.session = input.session = SessionValidator
					.lookupCheckAndExtend(input.session, "input.session");
		} catch (Exception e) {
			// session is not required for this call
		}

		if (input.pager == null) {
			input.pager = PagerHelper.createDefaultPager();
		} else {
			if (input.pager.start == null) {
				input.pager.start = PagerHelper.DEFAULT_START;
			}

			if (input.pager.count == null) {
				input.pager.count = PagerHelper.DEFAULT_COUNT;
			}
		}

		output.properties = PropertyServiceProvider.provide().getProperties(
				input.pager.start, input.pager.count,
				PropertySortType.fromString(input.pager.sortBy),
				input.pager.sortDirection);

		if (output.properties != null) {
			output.properties = new ArrayList<>(output.properties);
			appendBuildVersion(output.properties, input.pager.start,
					implementationBuild());
		}

		output.pager = PagerHelper.moveForward(input.pager);
	}

	private static volatile String cachedBuildVersion;

	static String implementationBuild () {
		if (cachedBuildVersion == null) {
			ServletContext context = BlogJsonServlet.CONTEXT.get();
			if (context != null) {
				try (InputStream in = context
						.getResourceAsStream("/META-INF/MANIFEST.MF")) {
					String build = readImplementationBuild(in);
					if (build != null) {
						cachedBuildVersion = build;
					}
				} catch (IOException e) {
					LOG.log(Level.FINE, "Could not read build version", e);
				}
			}
		}

		return cachedBuildVersion;
	}

	static String readImplementationBuild (InputStream in) throws IOException {
		if (in == null) {
			return null;
		}

		String build = new Manifest(in).getMainAttributes()
				.getValue("Implementation-Build");
		return build == null || build.trim().isEmpty() ? null : build.trim();
	}

	static void appendBuildVersion (List<Property> properties, Integer start,
			String version) {
		if (properties == null || version == null || version.trim().isEmpty()) {
			return;
		}
		if (start != null && start.intValue() != 0) {
			return;
		}

		properties.removeIf(p -> PropertyHelper.VERSION.equals(p.name));
		properties.add(new Property().name(PropertyHelper.VERSION)
				.type("string").value(version.trim()));
	}
	@Override
	public void clearSensitiveFields (GetPropertiesResponse output) {
		super.clearSensitiveFields(output);

		if (output.properties != null) {
			boolean admin = output.session != null
					&& UserValidator.isAdmin(output.session.user);
			Property property;
			String secrecy;
			for (int i = output.properties.size() - 1; i >= 0; i--) {
				property = output.properties.get(i);
				secrecy = PropertyHelper.secrecy(property);
				if (PropertyHelper.SECRECY_HIDDEN.equals(secrecy)
						|| (PropertyHelper.SECRECY_ADMIN.equals(secrecy)
								&& !admin)) {
					output.properties.remove(i);
				} else {
					property.secrecy = secrecy;
				}
			}
		}
	}

	@Override
	protected GetPropertiesResponse newOutput () {
		return new GetPropertiesResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
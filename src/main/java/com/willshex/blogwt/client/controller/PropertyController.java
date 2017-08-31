//
//  PropertyController.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.blog.BlogService;
import com.willshex.blogwt.client.api.blog.event.GetPropertiesEventHandler.GetPropertiesFailure;
import com.willshex.blogwt.client.api.blog.event.GetPropertiesEventHandler.GetPropertiesSuccess;
import com.willshex.blogwt.client.api.blog.event.SetupBlogEventHandler.SetupBlogFailure;
import com.willshex.blogwt.client.api.blog.event.SetupBlogEventHandler.SetupBlogSuccess;
import com.willshex.blogwt.client.api.blog.event.UpdatePropertiesEventHandler.UpdatePropertiesFailure;
import com.willshex.blogwt.client.api.blog.event.UpdatePropertiesEventHandler.UpdatePropertiesSuccess;
import com.willshex.blogwt.client.gwt.Window;
import com.willshex.blogwt.client.helper.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetPropertiesResponse;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogRequest;
import com.willshex.blogwt.shared.api.blog.call.SetupBlogResponse;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesRequest;
import com.willshex.blogwt.shared.api.blog.call.UpdatePropertiesResponse;
import com.willshex.blogwt.shared.api.datatype.Property;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.helper.PropertyHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PropertyController extends ListDataProvider<Property> {
	private static PropertyController one = null;

	/**
	 * @return
	 */
	public static PropertyController get () {
		if (one == null) {
			one = new PropertyController();
		}

		return one;
	}

	private Map<String, Property> propertyLookup = new HashMap<String, Property>();

	private Pager pager = PagerHelper.createInfinitePager();
	private Request getPropertiesRequest;

	private PropertyController () {
		String propertiesJson = Window.get().getProperties();

		if (propertiesJson != null) {
			JsonArray jsonPropertyArray = (new JsonParser())
					.parse(propertiesJson).getAsJsonArray();
			if (getList() == null) {
				setList(new ArrayList<Property>());
			} else {
				getList().clear();
			}

			Property item = null;
			for (int i = 0; i < jsonPropertyArray.size(); i++) {
				if (jsonPropertyArray.get(i).isJsonObject()) {
					(item = new Property()).fromJson(
							jsonPropertyArray.get(i).getAsJsonObject());
					propertyLookup.put(item.name, item);
					getList().add(item);
				}
			}
		}
	}

	public List<Property> blog () {
		return getList() == null || getList().isEmpty() ? null : getList();
	}

	/**
	 * @return
	 */
	public SafeUri copyrightHolderUrl () {
		Property p = propertyLookup.get(PropertyHelper.COPYRIGHT_URL);
		return UriUtils.fromSafeConstant(
				PropertyHelper.isEmpty(p) ? "https://www.willshex.com"
						: p.value);
	}

	/**
	 * @return
	 */
	public String copyrightHolder () {
		Property p = propertyLookup.get(PropertyHelper.COPYRIGHT_HOLDER);
		return PropertyHelper.isEmpty(p) ? "WillShex Limited" : p.value;
	}

	/**
	 * @return
	 */
	public String title () {
		Property p = propertyLookup.get(PropertyHelper.TITLE);
		return PropertyHelper.isEmpty(p) ? "Blogwt" : p.value;
	}

	/**
	 * @return
	 */
	public String extendedTitle () {
		Property p = propertyLookup.get(PropertyHelper.EXTENDED_TITLE);
		return PropertyHelper.isEmpty(p) ? "a blog is a blog" : p.value;
	}

	/**
	 * @return
	 */
	public Date started () {
		Property p = propertyLookup.get(PropertyHelper.TITLE);
		return PropertyHelper.isEmpty(p) ? new Date(1199188800000L) : p.created;
	}

	public void setupBlog (List<Property> properties, List<User> users) {
		final SetupBlogRequest input = ApiHelper
				.setAccessCode(new SetupBlogRequest()).properties(properties)
				.users(users);

		BlogService blogService = ApiHelper.createBlogClient();
		blogService.setupBlog(input, new AsyncCallback<SetupBlogResponse>() {

			@Override
			public void onSuccess (SetupBlogResponse output) {
				DefaultEventBus.get().fireEventFromSource(
						new SetupBlogSuccess(input, output),
						PropertyController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new SetupBlogFailure(input, caught),
						PropertyController.this);
			}
		});
	}

	/**
	 * @param name
	 * @return
	 */
	public String stringProperty (String name) {
		return name == null ? null
				: PropertyHelper.value(propertyLookup.get(name));
	}

	public boolean updateProperties (Collection<Property> properties) {
		List<Property> changed = null;

		String existingPropertyValue;
		boolean addToChanged;
		for (Property property : properties) {
			addToChanged = false;

			existingPropertyValue = PropertyHelper
					.value(propertyLookup.get(property.name));

			if (!PropertyHelper.isEmpty(property)) {
				if (existingPropertyValue == null) {
					addToChanged = true;
				} else if (!property.value.equals(existingPropertyValue)) {
					addToChanged = true;
				}
			}

			if (addToChanged) {
				if (changed == null) {
					changed = new ArrayList<Property>();
				}

				changed.add(property);
			}
		}

		boolean updating = changed != null;

		if (updating) {
			final UpdatePropertiesRequest input = SessionController.get()
					.setSession(ApiHelper
							.setAccessCode(new UpdatePropertiesRequest())
							.properties(changed));

			ApiHelper.createBlogClient().updateProperties(input,
					new AsyncCallback<UpdatePropertiesResponse>() {

						@Override
						public void onSuccess (
								UpdatePropertiesResponse output) {
							if (output != null
									&& output.status == StatusType.StatusTypeSuccess) {
								GWT.log("Properties have been updated successfully... reload to see the effects.");
							}

							DefaultEventBus.get().fireEventFromSource(
									new UpdatePropertiesSuccess(input, output),
									PropertyController.this);
						}

						@Override
						public void onFailure (Throwable caught) {
							DefaultEventBus.get().fireEventFromSource(
									new UpdatePropertiesFailure(input, caught),
									PropertyController.this);
						}
					});
		}

		return updating;
	}

	public void refreshProperties () {
		fetchProperties();
	}

	private void fetchProperties () {
		final GetPropertiesRequest input = ApiHelper
				.setAccessCode(new GetPropertiesRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		if (getPropertiesRequest != null) {
			getPropertiesRequest.cancel();
		}

		getPropertiesRequest = ApiHelper.createBlogClient().getProperties(input,
				new AsyncCallback<GetPropertiesResponse>() {

					@Override
					public void onSuccess (GetPropertiesResponse output) {
						getPropertiesRequest = null;

						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.properties != null
									&& output.properties.size() > 0) {
								pager = output.pager;

								JsonArray propertyArray = new JsonArray();
								for (Property p : output.properties) {
									propertyArray.add(p.toJson());
								}

								Window.get().setProperties(
										propertyArray.toString());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetPropertiesSuccess(input, output),
								PropertyController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						getPropertiesRequest = null;

						DefaultEventBus.get().fireEventFromSource(
								new GetPropertiesFailure(input, caught),
								PropertyController.this);
					}
				});
	}

	/**
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public boolean booleanProperty (String name, boolean defaultValue) {
		boolean value = defaultValue;

		String stringValue = stringProperty(name);

		if (stringValue != null) {
			value = Boolean.valueOf(stringValue);
		}

		return value;
	}
}

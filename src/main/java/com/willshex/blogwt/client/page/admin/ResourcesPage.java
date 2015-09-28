//
//  ResourcesPage.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 22 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.admin;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.PrettyButtonCell;
import com.willshex.blogwt.client.controller.ResourceController;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceRequest;
import com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest;
import com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse;
import com.willshex.blogwt.shared.api.blog.call.event.DeleteResourceEventHandler;
import com.willshex.blogwt.shared.api.blog.call.event.GetResourcesEventHandler;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.helper.DateTimeHelper;
import com.willshex.blogwt.shared.helper.PagerHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourcesPage extends Page implements GetResourcesEventHandler,
		DeleteResourceEventHandler {

	private static ResourcesPageUiBinder uiBinder = GWT
			.create(ResourcesPageUiBinder.class);

	interface ResourcesPageUiBinder extends UiBinder<Widget, ResourcesPage> {}

	interface Templates extends SafeHtmlTemplates {
		public static final Templates INSTANCE = GWT.create(Templates.class);

		@Template("<a target=\"_blank\" href=\"/upload?blob-key={0}\" alt=\"{1}\" title=\"{3}\"><span class=\"glyphicon glyphicon-download\"></span> {2}</a>")
		SafeHtml url (String key, String title, String text, String fullText);

		@Template("<img class=\"img-rounded\" src=\"/upload?blob-key={0}\" height=\"20px\" alt=\"{1}\" title=\"{1}\">")
		SafeHtml image (String key, String title);
	}

	@UiField(provided = true) CellTable<Resource> tblResources = new CellTable<Resource>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);

	@UiField SimplePager pgrResources;
	@UiField NoneFoundPanel pnlNoResources;

	private SafeHtmlCell safeHtmlPrototype = new SafeHtmlCell();
	private ButtonCell actionButtonPrototype = new PrettyButtonCell();

	public ResourcesPage () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		tblResources.setEmptyTableWidget(pnlNoResources);
		ResourceController.get().addDataDisplay(tblResources);
		pgrResources.setDisplay(tblResources);
	}

	private void createColumns () {

		Column<Resource, SafeHtml> preview = new Column<Resource, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Resource object) {
				return Templates.INSTANCE.image(object.data.substring(5),
						object.name);
			}
		};

		preview.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		TextColumn<Resource> name = new TextColumn<Resource>() {

			@Override
			public String getValue (Resource object) {
				return object.name;
			}
		};

		Column<Resource, SafeHtml> data = new Column<Resource, SafeHtml>(
				safeHtmlPrototype) {

			@Override
			public SafeHtml getValue (Resource object) {
				SafeHtml safeHtml = null;

				if (object.data == null) {
					safeHtml = SafeHtmlUtils.fromSafeConstant("none");
				} else {
					String text = object.data;

					if (text.length() > 10) {
						text = object.data.substring(0, 10);
						text += "...";
					}

					safeHtml = Templates.INSTANCE.url(object.data.substring(5),
							object.name, text, object.data);
				}

				return safeHtml;
			}
		};

		TextColumn<Resource> description = new TextColumn<Resource>() {

			@Override
			public String getValue (Resource object) {
				return object.description;
			}
		};

		TextColumn<Resource> type = new TextColumn<Resource>() {

			@Override
			public String getValue (Resource object) {
				return object.type == null ? "none" : object.type.toString();
			}
		};

		TextColumn<Resource> created = new TextColumn<Resource>() {

			@Override
			public String getValue (Resource object) {
				return DateTimeHelper.ago(object.created);
			}
		};

		TextColumn<Resource> properties = new TextColumn<Resource>() {

			@Override
			public String getValue (Resource object) {
				return object.properties == null ? "none" : object.properties;
			}
		};

		Column<Resource, String> delete = new Column<Resource, String>(
				actionButtonPrototype) {

			@Override
			public String getValue (Resource object) {
				return "delete";
			}
		};
		delete.setFieldUpdater(new FieldUpdater<Resource, String>() {

			@Override
			public void update (int index, Resource object, String value) {
				if (Window.confirm("Are you sure you want to delete resource: "
						+ object.name + "?")) {
					ResourceController.get().deleteResource(object);
				}
			}
		});

		tblResources.addColumn(preview);
		tblResources.addColumn(name, "Name");
		tblResources.addColumn(data, "Data");
		tblResources.addColumn(description, "Description");
		tblResources.addColumn(type, "Type");
		tblResources.addColumn(created, "Created");
		tblResources.addColumn(properties, "Properties");
		tblResources.addColumn(delete);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.client.page.Page#register(com.google.gwt.event.shared
	 * .HandlerRegistration) */
	@Override
	protected void register (HandlerRegistration registration) {
		super.register(registration);

		register(DefaultEventBus.get().addHandlerToSource(
				GetResourcesEventHandler.TYPE, ResourceController.get(), this));
		register(DefaultEventBus.get()
				.addHandlerToSource(DeleteResourceEventHandler.TYPE,
						ResourceController.get(), this));
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.DeleteResourceEventHandler
	 * #deleteResourceSuccess(com.willshex.blogwt.shared.api.blog.call.
	 * DeleteResourceRequest,
	 * com.willshex.blogwt.shared.api.blog.call.DeleteResourceResponse) */
	@Override
	public void deleteResourceSuccess (DeleteResourceRequest input,
			DeleteResourceResponse output) {}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.DeleteResourceEventHandler
	 * #deleteResourceFailure(com.willshex.blogwt.shared.api.blog.call.
	 * DeleteResourceRequest, java.lang.Throwable) */
	@Override
	public void deleteResourceFailure (DeleteResourceRequest input,
			Throwable caught) {

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.GetResourcesEventHandler
	 * #getResourcesSuccess
	 * (com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest,
	 * com.willshex.blogwt.shared.api.blog.call.GetResourcesResponse) */
	@Override
	public void getResourcesSuccess (GetResourcesRequest input,
			GetResourcesResponse output) {

	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.blogwt.shared.api.blog.call.event.GetResourcesEventHandler
	 * #getResourcesFailure
	 * (com.willshex.blogwt.shared.api.blog.call.GetResourcesRequest,
	 * java.lang.Throwable) */
	@Override
	public void getResourcesFailure (GetResourcesRequest input, Throwable caught) {

	}

}

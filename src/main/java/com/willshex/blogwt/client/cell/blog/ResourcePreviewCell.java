//
//  ResourcePreviewCell.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.blog;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.user.client.Window;
import com.willshex.blogwt.client.controller.ResourceController;
import com.willshex.blogwt.client.helper.PageTypeHelper;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.shared.api.datatype.Resource;
import com.willshex.blogwt.shared.api.upload.Upload;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourcePreviewCell extends AbstractCell<Resource> {

	public ResourcePreviewCell () {
		super(BrowserEvents.CLICK);
	}

	interface ResourcePreviewCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb, SafeHtml preview, SafeHtml download,
				String name, String description, SafeHtml edit,
				SafeHtml delete);

		void onBrowserEvent (ResourcePreviewCell resourcePreviewCell,
				NativeEvent event, Element parent, Resource value);
	}

	interface Templates extends SafeHtmlTemplates {
		public static final Templates INSTANCE = GWT.create(Templates.class);

		@Template("<a target=\"_blank\" href=\"" + Upload.PATH
				+ "?blob-key={0}\" alt=\"{1}\" title=\"{2}\" class=\"btn btn-default\"><span class=\"glyphicon glyphicon-download\"></span></a>")
		SafeHtml download (String key, String title, String fullText);

		@Template("<img class=\"img-rounded img-responsive center-block\" src=\"/upload?blob-key={0}\" alt=\"{1}\" title=\"{1}\">")
		SafeHtml image (String key, String title);

		@Template("<span class=\"glyphicon glyphicon-trash\"></span>")
		SafeHtml delete ();
	}

	private static ResourcePreviewCellRenderer RENDERER = GWT
			.create(ResourcePreviewCellRenderer.class);

	@Override
	public void onBrowserEvent (Context context, Element parent, Resource value,
			NativeEvent event, ValueUpdater<Resource> valueUpdater) {
		RENDERER.onBrowserEvent(this, event, parent, value);
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (com.google.gwt.cell.client.Cell.Context context,
			Resource value, SafeHtmlBuilder sb) {

		RENDERER.render(sb,
				Templates.INSTANCE.image(value.data.substring(5), value.name),
				Templates.INSTANCE.download(
						value.data.substring(5), value.name, value.data),
				value.name, value.description,
				UiHelper.TEMPLATES.edit(
						PageTypeHelper.asHref(PageType.EditResourcePageType,
								"id", value.id.toString())),
				Templates.INSTANCE.delete());
	}

	@UiHandler("btnDelete")
	void deleteClicked (ClickEvent event, Element parent, Resource value) {
		if (Window.confirm("Are you sure you want to delete resource "
				+ value.name + "?")) {
			ResourceController.get().deleteResource(value);
		}
	}
}

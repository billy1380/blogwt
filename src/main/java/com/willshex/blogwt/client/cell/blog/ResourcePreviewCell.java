//
//  ResourcePreviewCell.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.cell.blog;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourcePreviewCell extends AbstractCell<Resource> {

	interface ResourcePreviewCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb, SafeHtml preview, SafeHtml download,
				String name, String description, SafeHtml edit, SafeHtml delete);
	}

	interface Templates extends SafeHtmlTemplates {
		public static final Templates INSTANCE = GWT.create(Templates.class);

		@Template("<a target=\"_blank\" href=\"/upload?blob-key={0}\" alt=\"{1}\" title=\"{2}\" class=\"btn btn-default\"><span class=\"glyphicon glyphicon-download\"></span></a>")
		SafeHtml download (String key, String title, String fullText);

		@Template("<img class=\"img-rounded img-responsive center-block\" src=\"/upload?blob-key={0}\" alt=\"{1}\" title=\"{1}\">")
		SafeHtml image (String key, String title);

		@Template("<a class=\"btn btn-default\" href=\"{0}\" ><span class=\"glyphicon glyphicon-edit\"></span> edit</a>")
		SafeHtml edit (Long id);

		@Template("<a class=\"btn btn-danger\" ><span class=\"glyphicon glyphicon-trash\"></span></a>")
		SafeHtml delete ();
	}

	private static ResourcePreviewCellRenderer RENDERER = GWT
			.create(ResourcePreviewCellRenderer.class);

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client
	 * .Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder) */
	@Override
	public void render (com.google.gwt.cell.client.Cell.Context context,
			Resource value, SafeHtmlBuilder sb) {

		RENDERER.render(sb, Templates.INSTANCE.image(value.data.substring(5),
				value.name), Templates.INSTANCE.download(
				value.data.substring(5), value.name, value.data), value.name,
				value.description, Templates.INSTANCE.edit(value.id),
				Templates.INSTANCE.delete());
	}
}

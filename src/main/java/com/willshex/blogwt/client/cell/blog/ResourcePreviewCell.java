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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import com.willshex.blogwt.shared.api.datatype.Resource;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ResourcePreviewCell extends AbstractCell<Resource> {

	interface ResourcePreviewCellRenderer extends UiRenderer {
		void render (SafeHtmlBuilder sb);
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
		RENDERER.render(sb);
	}
}

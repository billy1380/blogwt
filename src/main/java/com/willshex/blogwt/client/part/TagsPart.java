//
//  TagsPart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 16 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.shared.api.datatype.Tag;

/**
 * @author William Shakour (billy1380)
 *
 */
public class TagsPart extends Composite {

	private static TagsPartUiBinder uiBinder = GWT
			.create(TagsPartUiBinder.class);

	interface TagsPartUiBinder extends UiBinder<Widget, TagsPart> {}

	@UiField(provided = true) CellList<Tag> clTags = new CellList<Tag>(
			new AbstractCell<Tag>() {

				@Override
				public void render (Context context, Tag value,
						SafeHtmlBuilder sb) {

				}

			}, BootstrapGwtCellList.INSTANCE);

	public TagsPart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

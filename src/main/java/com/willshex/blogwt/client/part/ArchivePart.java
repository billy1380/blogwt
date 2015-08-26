//
//  ArchivePart.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 23 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.controller.ArchiveEntryController;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ArchivePart extends Composite {

	private static ArchivePartUiBinder uiBinder = GWT
			.create(ArchivePartUiBinder.class);

	interface ArchivePartUiBinder extends UiBinder<Widget, ArchivePart> {}

	@UiField(provided = true) CellTree ctArchiveEntries;

	public ArchivePart () {
		ctArchiveEntries = new CellTree(
				new ArchiveEntryController.ArchiveModel(), null);
		initWidget(uiBinder.createAndBindUi(this));
		ctArchiveEntries.setAnimationEnabled(true);
	}
}

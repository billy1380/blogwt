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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ArchivePart extends Composite {

	private static ArchivePartUiBinder uiBinder = GWT
			.create(ArchivePartUiBinder.class);

	interface ArchivePartUiBinder extends UiBinder<Widget, ArchivePart> {}

	public ArchivePart () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

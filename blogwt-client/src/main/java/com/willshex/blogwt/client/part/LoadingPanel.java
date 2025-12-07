//
//  LoadingPanel.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 4 Oct 2015.
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
public class LoadingPanel extends Composite {

	private static LoadingPanelUiBinder uiBinder = GWT
			.create(LoadingPanelUiBinder.class);

	interface LoadingPanelUiBinder extends UiBinder<Widget, LoadingPanel> {}

	public LoadingPanel () {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

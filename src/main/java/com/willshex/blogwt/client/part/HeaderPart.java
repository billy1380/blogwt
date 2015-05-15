//
//  HeaderPart.java
//  blogwt
//
//  Created by billy1380 on 31 Jul 2013.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;

public class HeaderPart extends Composite {

	@UiField InlineHyperlink btnTests;
	@UiField DivElement divName;

	private static HeaderPartUiBinder uiBinder = GWT
			.create(HeaderPartUiBinder.class);

	interface HeaderPartUiBinder extends UiBinder<Widget, HeaderPart> {}

	public HeaderPart () {
		initWidget(uiBinder.createAndBindUi(this));

		Resources.RES.styles().ensureInjected();

		divName.setInnerText(PropertyController.get().title());
	}

	@UiHandler("btnExpand")
	void onBtnExpandClicked (ClickEvent event) {}

}

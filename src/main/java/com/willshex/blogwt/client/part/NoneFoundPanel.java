//
//  NoneFoundPanel.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Jun 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class NoneFoundPanel extends Composite {

	private static NoneFoundPanelUiBinder uiBinder = GWT
			.create(NoneFoundPanelUiBinder.class);

	interface NoneFoundPanelUiBinder extends UiBinder<Widget, NoneFoundPanel> {}

	@UiField Element elSubject;

	public NoneFoundPanel () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setSubject (String subject) {
		elSubject.setInnerText("No " + subject + "!");
	}

}

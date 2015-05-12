//
//  FooterPart.java
//  blogwt
//
//  Created by billy1380 on 31 Jul 2013.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FooterPart extends Composite {

	private static FooterPartUiBinder uiBinder = GWT
			.create(FooterPartUiBinder.class);

	interface FooterPartUiBinder extends UiBinder<Widget, FooterPart> {}

	@UiField DivElement divCopyright;

	public FooterPart () {
		initWidget(uiBinder.createAndBindUi(this));

		@SuppressWarnings("deprecation")
		String copyrightNotice = "Copyright &copy; <a href=\"http://www.willshex.com\" target=\"_blank\">WillShex Limited.</a> 2008-"
				+ (1900 + (new Date()).getYear())
				+ ". All rights reserved - blogwt.";

		divCopyright.setInnerHTML(copyrightNotice);
	}

}

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
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;
import com.willshex.blogwt.client.controller.PropertyController;
import com.willshex.blogwt.client.helper.ApiHelper;

public class FooterPart extends Composite {

	private static FooterPartUiBinder uiBinder = GWT
			.create(FooterPartUiBinder.class);

	interface FooterPartUiBinder extends UiBinder<Widget, FooterPart> {}

	public interface FooterTemplates extends SafeHtmlTemplates {
		FooterTemplates FOOTER_TEMPLATES = GWT.create(FooterTemplates.class);

		@Template("Copyright &copy; <a href=\"{0}\" class=\"{4}\" target=\"_blank\" rel=\"noopener\">{1}</a> {2}. All rights reserved - {3}.")
		SafeHtml copyrightNotice (SafeUri uri, String holder, String years,
				String name, String styleNames);

		@Template("Copyright &copy; <a href=\"{0}\">{1}</a> {2}. All rights reserved - {3}.")
		SafeHtml copyrightNoticeInternal (SafeUri uri, String holder,
				String years, String name);
	}

	@UiField DivElement divCopyright;
	@UiField BackToTop btnBackToTop;

	private FooterPart () {
		initWidget(uiBinder.createAndBindUi(this));

		PropertyController controller = PropertyController.get();

		SafeUri url = controller.copyrightHolderUrl();
		String urlAsString = url.asString();
		if (urlAsString.contains(ApiHelper.HOST)
				|| urlAsString.startsWith("#")) {
			divCopyright.setInnerSafeHtml(FooterTemplates.FOOTER_TEMPLATES
					.copyrightNoticeInternal(url, controller.copyrightHolder(),
							years(controller), controller.title()));
		} else {
			divCopyright.setInnerSafeHtml(FooterTemplates.FOOTER_TEMPLATES
					.copyrightNotice(url, controller.copyrightHolder(),
							years(controller), controller.title(),
							Resources.RES.styles().externalLink()));
		}
	}

	@SuppressWarnings("deprecation")
	private String years (PropertyController controller) {
		Date started = controller.started();
		Date now = new Date();

		if (started.getYear() == now.getYear()) {
			return Integer.toString(1900 + now.getYear());
		} else {
			return Integer.toString(1900 + started.getYear()) + "-"
					+ Integer.toString(1900 + now.getYear());
		}
	}

	private static FooterPart one;

	/**
	 * @return
	 */
	public static FooterPart get () {
		if (one == null) {
			one = new FooterPart();
		}

		return one;
	}

	public void scrollToTop () {
		btnBackToTop.go();
	}

}

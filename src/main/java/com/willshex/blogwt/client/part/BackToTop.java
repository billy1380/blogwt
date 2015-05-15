//
//  BackToTop.java
//  com.willshex.codegen
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BackToTop extends Composite {

	private static BackToTopUiBinder uiBinder = GWT
			.create(BackToTopUiBinder.class);

	interface BackToTopUiBinder extends UiBinder<Widget, BackToTop> {}

	@UiField Button btnBackToTop;

	public BackToTop () {
		initWidget(uiBinder.createAndBindUi(this));

		btnBackToTop
				.getElement()
				.setInnerHTML(
						SafeHtmlUtils
								.fromTrustedString(
										"<span class=\"glyphicon glyphicon-arrow-up aria-hidden=\"true\"></span> ")
								.asString()
								+ btnBackToTop.getText());
	}

	@UiHandler("btnBackToTop")
	void onBtnBackToTopClicked (ClickEvent e) {
		Window.scrollTo(0, 0);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		Window.addWindowScrollHandler(new ScrollHandler() {

			@Override
			public void onWindowScroll (ScrollEvent event) {
				btnBackToTop.setVisible(event.getScrollTop() > 200);
			}
		});
	}
}

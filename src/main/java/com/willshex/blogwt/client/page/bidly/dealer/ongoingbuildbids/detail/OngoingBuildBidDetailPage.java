//
//  OngoingBuildBidDetailPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 29 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.ongoingbuildbids.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.bidly.dummy.data.Offers;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;
import com.willshex.blogwt.shared.helper.bidly.AddressHelper;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class OngoingBuildBidDetailPage extends Page {

	private static OngoingBuildBidDetailPageUiBinder uiBinder = GWT
			.create(OngoingBuildBidDetailPageUiBinder.class);

	interface OngoingBuildBidDetailPageUiBinder
			extends UiBinder<Widget, OngoingBuildBidDetailPage> {}

	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("{0} <span class=\"h4\"><a target=\"_blank\" href=\"http://google.com/maps?q={1}\"><span class=\"glyphicon glyphicon-map-marker\"></span> {1}</a></span>")
		SafeHtml title (String name, String postcode);

		@Template("Offer <span class=\"h4\">{0}</span>")
		SafeHtml title (Long id);
	}

	@UiField Element elTitle;

	public OngoingBuildBidDetailPage () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this::onNavigationChanged));
	}

	private void onNavigationChanged (Stack p, Stack c) {
		if ("id".equals(c.getAction()) && c.getParameterCount() > 0) {
			String param = c.getParameter(0);
			Long id = Long.valueOf(param);

			elTitle.setInnerSafeHtml(Templates.T.title(id));

			showOffer(Offers.LOOKUP.get(id));
		}
	}

	private void showOffer (Offer offer) {
		elTitle.setInnerSafeHtml(Templates.T.title(offer.user.surname,
				AddressHelper.postcodeArea(offer.address.postcode)));
	}

}

//
//  BuildBidDetailPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 28 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.buildbids.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.bidly.dummy.data.Offers;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;
import com.willshex.blogwt.client.part.bidly.bidtoohigh.BidTooHighDialog;
import com.willshex.blogwt.shared.helper.bidly.AddressHelper;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BuildBidDetailPage extends Page {

	private static BuildBidDetailPageUiBinder uiBinder = GWT
			.create(BuildBidDetailPageUiBinder.class);

	interface BuildBidDetailPageUiBinder
			extends UiBinder<Widget, BuildBidDetailPage> {}

	interface Style extends CssResource {
		String red ();

		String green ();
	}

	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("{0} <span class=\"h4\"><a target=\"_blank\" href=\"http://google.com/maps?q={1}\"><span class=\"glyphicon glyphicon-map-marker\"></span> {1}</a></span>")
		SafeHtml title (String name, String postcode);

		@Template("Offer <span class=\"h4\">{0}</span>")
		SafeHtml title (Long id);
	}

	private static final NumberFormat FORMATTER = NumberFormat
			.getFormat("#,###");

	@UiField Element elTitle;

	@UiField Element elOtr;
	@UiField Element elNewOtr;
	@UiField Element elBid;
	@UiField Element elStatus;

	@UiField Button btnIncrease1;
	@UiField Button btnIncrease2;
	@UiField Button btnIncrease3;
	@UiField Button btnIncrease4;
	@UiField Element elIncrease1;
	@UiField Element elIncrease2;
	@UiField Element elIncrease3;
	@UiField Element elIncrease4;

	@UiField Style style;

	public BuildBidDetailPage () {
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

	@UiHandler({ "btnIncrease1", "btnIncrease2", "btnIncrease3",
			"btnIncrease4" })
	void onIncreaseClicked (ClickEvent ce) {
		String amountParam = null;

		if (ce.getSource() == btnIncrease1) {
			amountParam = elIncrease1.getInnerText();
		} else if (ce.getSource() == btnIncrease2) {
			amountParam = elIncrease2.getInnerText();
		} else if (ce.getSource() == btnIncrease3) {
			amountParam = elIncrease3.getInnerText();
		} else if (ce.getSource() == btnIncrease4) {
			amountParam = elIncrease4.getInnerText();
		}

		if (amountParam != null) {
			if (Window.confirm(
					"Are you sure you would like to increase your bid by £"
							+ amountParam + "?")) {
				double amount = FORMATTER.parse(amountParam);
				double bid = FORMATTER.parse(elBid.getInnerText());
				double otr = FORMATTER.parse(elOtr.getInnerText());
				double newBid;

				elBid.setInnerText(FORMATTER.format(newBid = bid + amount));
				elNewOtr.setInnerText(FORMATTER.format(otr - newBid));

				elStatus.removeClassName(style.red());
				elStatus.removeClassName(style.green());

				if (newBid > 1400) {
					elStatus.setInnerText("winning");
					elStatus.addClassName(style.green());
				} else {
					elStatus.setInnerText("losing");
					elStatus.addClassName(style.red());
				}
			}
		}
	}

	@UiHandler("btnBidTooHigh")
	void onBidTooHighClicked (ClickEvent ce) {
		RootPanel.get().add(new BidTooHighDialog().show(r -> {
			if (r.intValue() == 1) {
				Window.alert("Thank you. Your bid has now been submitted.");
			}
		}));
	}
}

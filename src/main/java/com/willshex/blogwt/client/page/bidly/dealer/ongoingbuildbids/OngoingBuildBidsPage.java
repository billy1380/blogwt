//
//  OngoingBuildBidsPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.ongoingbuildbids;

import static com.willshex.blogwt.client.helper.UiHelper.activateItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.cell.bidly.offerpreview.OfferPreviewCell;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.helper.bidly.PageTypeHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.bidly.dummy.data.Offers;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Offer;
import com.willshex.blogwt.client.part.BootstrapGwtCellList;
import com.willshex.blogwt.client.part.LoadingPanel;
import com.willshex.blogwt.client.part.NoneFoundPanel;
import com.willshex.blogwt.client.part.SimplePager;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class OngoingBuildBidsPage extends Page {

	private static OngoingBuildBidsPageUiBinder uiBinder = GWT
			.create(OngoingBuildBidsPageUiBinder.class);

	interface OngoingBuildBidsPageUiBinder
			extends UiBinder<Widget, OngoingBuildBidsPage> {}

	@UiField InlineHyperlink lnkAll;
	@UiField InlineHyperlink lnkAccepted;
	@UiField InlineHyperlink lnkExpired;
	@UiField InlineHyperlink lnkNegotiating;

	@UiField(provided = true) CellList<Offer> clOffers = new CellList<Offer>(
			new OfferPreviewCell(), BootstrapGwtCellList.INSTANCE, i -> i.id);

	@UiField Button btnRefresh;
	@UiField SimplePager pgrBottom;
	@UiField NoneFoundPanel pnlNoOffers;
	@UiField LoadingPanel pnlLoading;

	private SingleSelectionModel<Offer> model = new SingleSelectionModel<>();

	private final ListDataProvider<Offer> ALL = new ListDataProvider<>(
			Offers.AEN);
	private final ListDataProvider<Offer> ACCEPTED = new ListDataProvider<>(
			Offers.ACCEPTED);
	private final ListDataProvider<Offer> EXPIRED = new ListDataProvider<>(
			Offers.EXPIRED);
	private final ListDataProvider<Offer> NEGOTIATING = new ListDataProvider<>(
			Offers.NEGOTIATING);

	private ListDataProvider<Offer> current = ALL;

	public OngoingBuildBidsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		clOffers.setSelectionModel(model);
		clOffers.setPageSize(PagerHelper.DEFAULT_COUNT.intValue());
		clOffers.setEmptyListWidget(pnlNoOffers);
		clOffers.setLoadingIndicator(pnlLoading);
		current.addDataDisplay(clOffers);
		pgrBottom.setDisplay(clOffers);
		pgrBottom.setPageSize(12);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.client.page.Page#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(model.addSelectionChangeHandler(e -> {
			if (e.getSource() == model) {
				Offer selection = model.getSelectedObject();

				if (selection != null) {
					model.setSelected(selection, false);

					PageTypeHelper.show(PageType.OngoingBuildBidDetailPageType,
							"id", selection.id.toString());

				}
			}
		}));

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> updateViews(c.getAction())));
	}

	private void updateViews (String action) {
		current.removeDataDisplay(clOffers);

		activateItem(null, false, pp -> lnkAll.getElement().getParentElement());
		activateItem(null, false,
				pp -> lnkAccepted.getElement().getParentElement());
		activateItem(null, false,
				pp -> lnkExpired.getElement().getParentElement());
		activateItem(null, false,
				pp -> lnkNegotiating.getElement().getParentElement());

		if ("accepted".equals(action)) {
			current = ACCEPTED;
			activateItem(null, true,
					pp -> lnkAccepted.getElement().getParentElement());
		} else if ("expired".equals(action)) {
			current = EXPIRED;
			activateItem(null, true,
					pp -> lnkExpired.getElement().getParentElement());
		} else if ("negotiation".equals(action)) {
			current = NEGOTIATING;
			activateItem(null, true,
					pp -> lnkNegotiating.getElement().getParentElement());
		} else {
			current = ALL;
			activateItem(null, true,
					pp -> lnkAll.getElement().getParentElement());
		}
		current.addDataDisplay(clOffers);
	}

}

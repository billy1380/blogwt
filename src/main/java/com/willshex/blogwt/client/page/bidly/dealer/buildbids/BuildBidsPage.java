//
//  BuildBidsPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.buildbids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.willshex.blogwt.client.cell.bidly.offerpreview.OfferPreviewCell;
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
public class BuildBidsPage extends Page {

	private static BuildBidsPageUiBinder uiBinder = GWT
			.create(BuildBidsPageUiBinder.class);

	interface BuildBidsPageUiBinder extends UiBinder<Widget, BuildBidsPage> {}

	@UiField(provided = true) CellList<Offer> clOffers = new CellList<Offer>(
			new OfferPreviewCell(), BootstrapGwtCellList.INSTANCE, i -> i.id);

	@UiField Button btnRefresh;
	@UiField SimplePager pgrBottom;
	@UiField NoneFoundPanel pnlNoOffers;
	@UiField LoadingPanel pnlLoading;

	private SingleSelectionModel<Offer> model = new SingleSelectionModel<>();

	public BuildBidsPage () {
		initWidget(uiBinder.createAndBindUi(this));

		clOffers.setSelectionModel(model);
		clOffers.setPageSize(PagerHelper.DEFAULT_COUNT.intValue());
		clOffers.setEmptyListWidget(pnlNoOffers);
		clOffers.setLoadingIndicator(pnlLoading);
		new ListDataProvider<>(Offers.NEW).addDataDisplay(clOffers);
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

					PageTypeHelper.show(PageType.BuildBidDetailPageType, "id",
							selection.id.toString());

				}
			}
		}));
	}

}

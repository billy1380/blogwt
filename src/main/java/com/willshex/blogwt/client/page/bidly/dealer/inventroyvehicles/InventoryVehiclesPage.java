//
//  InventoryVehiclesPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 27 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.inventroyvehicles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.willshex.blogwt.client.cell.bidly.vehiclepreview.VehiclePreviewCell;
import com.willshex.blogwt.client.helper.bidly.PageTypeHelper;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.bidly.dummy.data.Garage;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Vehicle;
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
public class InventoryVehiclesPage extends Page {

	private static InventoryVehiclesPageUiBinder uiBinder = GWT
			.create(InventoryVehiclesPageUiBinder.class);

	interface InventoryVehiclesPageUiBinder
			extends UiBinder<Widget, InventoryVehiclesPage> {}

	@UiField(provided = true) CellList<Vehicle> clVehicles = new CellList<>(
			new VehiclePreviewCell(), BootstrapGwtCellList.INSTANCE, i -> i.id);

	@UiField Button btnRefresh;
	@UiField SimplePager pgrBottom;
	@UiField NoneFoundPanel pnlNoOffers;
	@UiField LoadingPanel pnlLoading;

	private SingleSelectionModel<Vehicle> model = new SingleSelectionModel<>();

	public InventoryVehiclesPage () {
		initWidget(uiBinder.createAndBindUi(this));

		clVehicles.setSelectionModel(model);
		clVehicles.setPageSize(PagerHelper.DEFAULT_COUNT.intValue());
		clVehicles.setEmptyListWidget(pnlNoOffers);
		clVehicles.setLoadingIndicator(pnlLoading);
		new ListDataProvider<>(Garage.ALL).addDataDisplay(clVehicles);
		pgrBottom.setDisplay(clVehicles);
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
				Vehicle selection = model.getSelectedObject();

				if (selection != null) {
					model.setSelected(selection, false);

					PageTypeHelper.show(PageType.VehicleDetailPageType, "id",
							selection.id.toString());

				}
			}
		}));
	}
}

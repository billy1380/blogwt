//
//  VehicleDetailPage.java
//  bidly
//
//  Created by William Shakour (billy1380) on 3 Jan 2018.
//  Copyright © 2018 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.page.bidly.dealer.inventroyvehicles.detail;

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
import com.willshex.blogwt.client.page.bidly.dummy.data.Garage;
import com.willshex.blogwt.client.page.bidly.dummy.datatypes.Vehicle;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class VehicleDetailPage extends Page {

	private static VehicleDetailPageUiBinder uiBinder = GWT
			.create(VehicleDetailPageUiBinder.class);

	interface VehicleDetailPageUiBinder
			extends UiBinder<Widget, VehicleDetailPage> {}

	interface Templates extends SafeHtmlTemplates {
		public static final Templates T = GWT.create(Templates.class);

		@Template("{0} <span class=\"h4\">{1}</span> <br /> <span class=\"h3\" style=\"font-family:'Courier New',Courier,'Lucida Sans Typewriter','Lucida Typewriter',monospace;font-weight:900;\">{2}</span>")
		SafeHtml title (String make, String model, String registration);

		@Template("Vehicle <span class=\"h4\">{0}</span>")
		SafeHtml title (Long id);
	}

	@UiField Element elTitle;

	public VehicleDetailPage () {
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

			showVehicle(Garage.LOOKUP.get(id));
		}
	}

	private void showVehicle (Vehicle vehicle) {
		elTitle.setInnerSafeHtml(Templates.T.title(vehicle.build.make,
				vehicle.build.model, vehicle.registration));
	}

}

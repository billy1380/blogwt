//
//  SideNav.java
//  bidly
//
//  Created by William Shakour (billy1380) on 21 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.bidly.sidenav;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.gwt.RegisteringComposite;
import com.willshex.blogwt.client.helper.bidly.PageTypeHelper;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SideNav extends RegisteringComposite {

	private static SideNavUiBinder uiBinder = GWT.create(SideNavUiBinder.class);

	interface SideNavUiBinder extends UiBinder<Widget, SideNav> {}

	@UiField Button btnToggle;
	@UiField Element elDirection;
	@UiField Element elDashboard;
	@UiField Element elBuildBids;
	@UiField Element elOngoingBuildBids;
	@UiField Element elUploadInventory;
	@UiField Element elInventoryBids;

	private boolean open = true;
	private Map<String, Element> pages = new HashMap<>();

	public SideNav () {
		initWidget(uiBinder.createAndBindUi(this));
		pages.put(PageTypeHelper.DASHBOARD_PAGE_HREF.asString(),
				elDashboard.getParentElement());
		pages.put(PageTypeHelper.BUILD_BIDS_PAGE_HREF.asString(),
				elBuildBids.getParentElement());
		pages.put(PageTypeHelper.ONGOING_BUILD_BIDS_PAGE_HREF.asString(),
				elOngoingBuildBids.getParentElement());
		pages.put(PageTypeHelper.UPLOAD_INVENTORY_PAGE_HREF.asString(),
				elUploadInventory.getParentElement());
		pages.put(PageTypeHelper.INVENTORY_BIDS_PAGE_HREF.asString(),
				elInventoryBids.getParentElement());
	}

	@UiHandler("btnToggle")
	void onToggleClicked (ClickEvent ce) {
		open = !open;
		update();
		Cookies.setCookie("side_nav", Boolean.toString(open), DateTimeHelper
				.millisFromNow(DateTimeHelper.MILLIS_PER_DAY * 365L * 20L));
	}

	void update () {
		if (open) {
			elDashboard.getStyle().clearDisplay();
			elBuildBids.getStyle().clearDisplay();
			elOngoingBuildBids.getStyle().clearDisplay();
			elUploadInventory.getStyle().clearDisplay();
			elInventoryBids.getStyle().clearDisplay();
			getElement().getStyle().setPaddingRight(10, Unit.PX);
			elDirection.setClassName("glyphicon glyphicon-triangle-left");
		} else {
			elDashboard.getStyle().setDisplay(Display.NONE);
			elBuildBids.getStyle().setDisplay(Display.NONE);
			elOngoingBuildBids.getStyle().setDisplay(Display.NONE);
			elUploadInventory.getStyle().setDisplay(Display.NONE);
			elInventoryBids.getStyle().setDisplay(Display.NONE);
			getElement().getStyle().setPaddingRight(0, Unit.PX);
			elDirection.setClassName("glyphicon glyphicon-triangle-right");
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		String cookieOpen;
		if ((cookieOpen = Cookies.getCookie("side_nav")) == null) {} else {
			open = Boolean.parseBoolean(cookieOpen);
			update();
		}

		register(DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				(p, c) -> {
					if (p != null) {
						activateItem(p.getPage(), false);
					}

					activateItem(c.getPage(), true);
				}));
	}

	/**
	 * @param page
	 * @param b
	 */
	private void activateItem (String page, boolean active) {
		Element e = pages.get("#" + page);

		if (e != null) {
			if (active) {
				e.addClassName("active");
			} else {
				e.removeClassName("active");
			}
		}
	}

}

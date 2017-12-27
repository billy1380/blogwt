//
//  PageTypeHelper.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper.bidly;

import com.google.gwt.safehtml.shared.SafeUri;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.page.admin.bidly.changedealerdetails.ChangeDealerDetailsPage;
import com.willshex.blogwt.client.page.bidly.dealer.buildbids.BuildBidsPage;
import com.willshex.blogwt.client.page.bidly.dealer.dashboard.DashboardPage;
import com.willshex.blogwt.client.page.bidly.dealer.inventorybids.InventoryBidsPage;
import com.willshex.blogwt.client.page.bidly.dealer.ongoingbuildbids.OngoingBuildBidsPage;
import com.willshex.blogwt.client.page.bidly.dealer.uploadinventory.UploadInventoryPage;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageTypeHelper
		extends com.willshex.blogwt.client.helper.PageTypeHelper {

	public static final SafeUri DASHBOARD_PAGE_HREF = asHref(
			PageType.DashboardPageType);

	public static final SafeUri BUILD_BIDS_PAGE_HREF = asHref(
			PageType.BuildBidsPageType);

	public static final SafeUri INVENTORY_BIDS_PAGE_HREF = asHref(
			PageType.InventoryBidsPageType);

	public static final SafeUri ONGOING_BUILD_BIDS_PAGE_HREF = asHref(
			PageType.OngoingBuildBidsPageType);

	public static final SafeUri UPLOAD_INVENTORY_PAGE_HREF = asHref(
			PageType.UploadInventoryPageType);

	public static Page createPage (PageType pageType) {
		Page page;

		switch (pageType) {
		case ChangeDealerDetailsPageType:
			page = new ChangeDealerDetailsPage();
			break;
		case DashboardPageType:
			page = new DashboardPage();
			break;
		case BuildBidsPageType:
			page = new BuildBidsPage();
			break;
		case OngoingBuildBidsPageType:
			page = new OngoingBuildBidsPage();
			break;
		case UploadInventoryPageType:
			page = new UploadInventoryPage();
			break;
		case InventoryBidsPageType:
			page = new InventoryBidsPage();
			break;
		default:
			page = com.willshex.blogwt.client.helper.PageTypeHelper
					.createPage(pageType);
			break;
		}

		return page;
	}
}

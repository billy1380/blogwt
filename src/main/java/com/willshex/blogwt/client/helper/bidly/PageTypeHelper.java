//
//  PageTypeHelper.java
//  bidly
//
//  Created by William Shakour (billy1380) on 20 Dec 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.helper.bidly;

import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.shared.page.PageType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PageTypeHelper
		extends com.willshex.blogwt.client.helper.PageTypeHelper {
	public static Page createPage (PageType pageType) {
		Page page;

		switch (pageType) {
		case DashboardPageType:
			page = null;
			break;
		case BuildBidsPageType:
			page = null;
			break;
		case OngoingBuildBidsPageType:
			page = null;
			break;
		case UploadInventoryPageType:
			page = null;
			break;
		case InventoryBids:
			page = null;
			break;
		default:
			page = com.willshex.blogwt.client.helper.PageTypeHelper
					.createPage(pageType);
			break;
		}

		return page;
	}
}

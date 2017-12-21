// 
//  DealerServiceProvider.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.dealer;

import com.willshex.service.ServiceDiscovery;

final public class DealerServiceProvider {

	/**
	* @return
	*/
	public static IDealerService provide () {
		IDealerService dealerService = null;

		if ((dealerService = (IDealerService) ServiceDiscovery
				.getService(IDealerService.NAME)) == null) {
			dealerService = DealerServiceFactory.createNewDealerService();
			ServiceDiscovery.registerService(dealerService);
		}

		return dealerService;
	}

}
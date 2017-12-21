// 
//  DealerServiceFactory.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.dealer;

final class DealerServiceFactory {

	/**
	* @return
	*/
	public static IDealerService createNewDealerService () {
		return new DealerService();
	}

}
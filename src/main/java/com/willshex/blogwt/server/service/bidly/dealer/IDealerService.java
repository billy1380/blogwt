// 
//  IDealerService.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.dealer;

import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.datatype.bidly.Dealer;
import com.willshex.blogwt.shared.api.datatype.bidly.DealerSortBy;
import com.willshex.service.IService;

public interface IDealerService extends IService, ISortable<DealerSortBy> {

	public static final String NAME = "bidly.dealer";

	/**
	* @param id
	* @return
	*/
	public Dealer getDealer (Long id);

	/**
	* @param dealer
	* @return
	*/
	public Dealer addDealer (Dealer dealer);

	/**
	* @param dealer
	* @return
	*/
	public Dealer updateDealer (Dealer dealer);

}
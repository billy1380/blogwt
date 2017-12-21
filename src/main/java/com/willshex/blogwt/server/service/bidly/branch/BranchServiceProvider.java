// 
//  BranchServiceProvider.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.branch;

import com.willshex.service.ServiceDiscovery;

final public class BranchServiceProvider {

	/**
	* @return
	*/
	public static IBranchService provide () {
		IBranchService branchService = null;

		if ((branchService = (IBranchService) ServiceDiscovery
				.getService(IBranchService.NAME)) == null) {
			branchService = BranchServiceFactory.createNewBranchService();
			ServiceDiscovery.registerService(branchService);
		}

		return branchService;
	}

}
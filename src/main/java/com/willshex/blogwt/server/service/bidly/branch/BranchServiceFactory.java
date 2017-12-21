// 
//  BranchServiceFactory.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.branch;

final class BranchServiceFactory {

	/**
	* @return
	*/
	public static IBranchService createNewBranchService () {
		return new BranchService();
	}

}
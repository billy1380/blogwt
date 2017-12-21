// 
//  IBranchService.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.branch;

import com.willshex.blogwt.server.service.ISortable;
import com.willshex.blogwt.shared.api.datatype.bidly.Branch;
import com.willshex.blogwt.shared.api.datatype.bidly.BranchSortBy;
import com.willshex.service.IService;

public interface IBranchService extends IService, ISortable<BranchSortBy> {

	public static final String NAME = "bidly.branch";

	/**
	* @param id
	* @return
	*/
	public Branch getBranch (Long id);

	/**
	* @param branch
	* @return
	*/
	public Branch addBranch (Branch branch);

	/**
	* @param branch
	* @return
	*/
	public Branch updateBranch (Branch branch);

}
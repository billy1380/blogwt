// 
//  BranchService.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.branch;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.shared.api.datatype.bidly.Branch;
import com.willshex.blogwt.shared.api.datatype.bidly.BranchSortBy;

final class BranchService implements IBranchService {
	public String getName () {
		return NAME;
	}

	@Override
	public Branch getBranch (Long id) {
		return load().id(id.longValue()).now();
	}

	public LoadType<Branch> load () {
		return provide().load().type(Branch.class);
	}

	@Override
	public Branch addBranch (Branch branch) {
		if (branch.created == null) {
			branch.created = new Date();
		}

		Key<Branch> key = provide().save().entity(branch).now();
		branch.id = Long.valueOf(key.getId());

		return branch;
	}

	@Override
	public Branch updateBranch (Branch branch) {
		provide().save().entity(branch).now();
		return branch;
	}

	/* (non-Javadoc)
	 * 
	 * @see com.willshex.blogwt.server.service.ISortable#map(java.lang.Enum) */
	@Override
	public String map (BranchSortBy sortBy) {
		String mapped = sortBy.toString();

		if (sortBy == BranchSortBy.BranchSortByDealer) {
			mapped += "Key";
		}

		return mapped;
	}

}
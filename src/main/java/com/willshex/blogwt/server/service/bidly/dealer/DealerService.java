// 
//  DealerService.java
//  bidly
// 
//  Created by William Shakour on December 20, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 

package com.willshex.blogwt.server.service.bidly.dealer;

import static com.willshex.blogwt.server.service.persistence.PersistenceServiceProvider.provide;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.willshex.blogwt.shared.api.datatype.bidly.Dealer;

final class DealerService implements IDealerService {
	public String getName () {
		return NAME;
	}

	@Override
	public Dealer getDealer (Long id) {
		return load().id(id.longValue()).now();
	}

	private LoadType<Dealer> load () {
		return provide().load().type(Dealer.class);
	}

	@Override
	public Dealer addDealer (Dealer dealer) {
		if (dealer.created == null) {
			dealer.created = new Date();
		}

		Key<Dealer> key = provide().save().entity(dealer).now();
		dealer.id = Long.valueOf(key.getId());

		return dealer;
	}

	@Override
	public Dealer updateDealer (Dealer dealer) {
		provide().save().entity(dealer).now();

		return dealer;
	}

}
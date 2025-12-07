//
//  ISearch.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Jul 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.search;

import java.util.Collection;
import java.util.List;

import com.willshex.blogwt.shared.api.SortDirectionType;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface ISearch<T> extends IIndex<T> {

	List<T> search (String query, Integer start, Integer count, String sortBy,
			SortDirectionType direction);

	String search (Collection<T> resultHolder, String query, String next,
			Integer count, String sortBy, SortDirectionType direction);
	@Override
	default boolean isShared () {
		return false;
	}

}

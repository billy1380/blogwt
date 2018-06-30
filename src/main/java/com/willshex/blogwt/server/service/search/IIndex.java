//
//  IIndex.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 25 Jul 2016.
//  Copyright © 2016 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service.search;

import com.google.appengine.api.search.Document;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface IIndex<T> {

	default boolean isShared() {
		return false;
	}
	
	void index (Long id);

	void indexAll ();

	Document toDocument (T instance);

}

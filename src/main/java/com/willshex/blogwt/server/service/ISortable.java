//
//  ISortable.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 13 Apr 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.service;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface ISortable<T extends Enum<T>> {
	public default String map (T sortBy) {
		String mapped = sortBy.toString();

		if ("id".equals(mapped)) {
			mapped = "__key__";
		}

		return mapped;
	}
}

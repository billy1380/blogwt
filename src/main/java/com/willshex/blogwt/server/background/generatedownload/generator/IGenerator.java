//
//  IGenerator.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.background.generatedownload.generator;

import com.willshex.blogwt.shared.api.datatype.GeneratedDownload;
import com.willshex.blogwt.shared.page.search.Filter;

/**
 * @author William Shakour (billy1380)
 *
 */
@FunctionalInterface
public interface IGenerator {

	/**
	 * @param download
	 * @param stack
	 * @param filter
	 */
	public byte[] generate (GeneratedDownload download, Filter filter);
}

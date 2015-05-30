//
//  PostHelper.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 30 May 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.helper;

import org.markdown4j.client.MarkdownProcessor;

/**
 * @author William Shakour (billy1380)
 *
 */
public class PostHelper extends
		com.willshex.blogwt.shared.api.helper.PostHelper {

	private static MarkdownProcessor processor;

	private static MarkdownProcessor processor () {
		if (processor == null) {
			processor = new MarkdownProcessor();
		}

		return processor;
	}

	public static String createMarkup (String value) {
		return processor().process(value);
	}

}

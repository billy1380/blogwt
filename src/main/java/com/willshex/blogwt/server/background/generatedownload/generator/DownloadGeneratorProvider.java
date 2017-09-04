//
//  GeneratorFactory.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 1 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.server.background.generatedownload.generator;

import java.util.function.Supplier;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DownloadGeneratorProvider {

	/**
	 * @param type
	 * @return
	 */
	public static IGenerator generator (String type) {
		IGenerator g = null;

		switch (type) {
		default:
			break;
		}
		return g;
	}

	public static Supplier<String> extension (String type) {
		Supplier<String> e = null;

		switch (type) {
		default:
			break;
		}
		return e;
	}

	public static Supplier<String> contentType (String type) {
		Supplier<String> c = null;

		switch (type) {
		default:
			break;
		}
		return c;
	}

}

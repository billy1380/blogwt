//
//  SandboxGwtTest.java
//  com.willshex.blogwt
//
//  Created by William Shakour (billy1380) on 11 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client;

import com.google.gwt.junit.client.GWTTestCase;
/**
 * 
 * @author William Shakour (billy1380)
 *
 */
public class SandboxGwtTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.willshex.blogwt.Blogwt";
	}

	public void testSandbox() {
		assertTrue(true);
	}

}

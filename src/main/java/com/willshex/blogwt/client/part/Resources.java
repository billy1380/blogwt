//
//  Resources.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 20 Jul 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface Resources extends ClientBundle {
	public interface ResourceStyles extends CssResource {
		String disqusLogo ();
	}

	public static final Resources RES = GWT.create(Resources.class);

	@Source("res/styles.gss")
	ResourceStyles styles ();

	@Source("res/disquslogo.png")
	ImageResource disqusLogo ();
}

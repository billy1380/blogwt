//
//  BootstrapGwtSuggestBox.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 3 Oct 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author William Shakour (billy1380)
 *
 */
public interface BootstrapGwtSuggestBox extends ClientBundle {
	public static final BootstrapGwtSuggestBox INSTANCE = GWT
			.create(BootstrapGwtSuggestBox.class);

	public interface BootstrapGwtSuggestBoxStyle extends CssResource {};

	@Source("res/bootstrap-gwt-suggestbox.gss")
	BootstrapGwtSuggestBoxStyle styles ();
}

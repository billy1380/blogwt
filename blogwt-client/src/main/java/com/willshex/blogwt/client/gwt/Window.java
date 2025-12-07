//
//  Window.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 31 Aug 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.gwt;

import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsProperty;
import jsinterop.base.Js;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Window extends elemental2.dom.Window {

	@JsProperty(name = "tags")
	public native String getTags ();

	@JsProperty(name = "archiveEntries")
	public native String getArchiveEntries ();

	@JsProperty(name = "pages")
	public native String getPages ();

	@JsProperty(name = "properties")
	public native String getProperties ();

	@JsProperty(name = "properties")
	public native void setProperties (String properties);

	@JsProperty(name = "session")
	public native String getSession ();

	@JsProperty(name = "session")
	public native void setSession (String session);

	public static Window get () {
		return Js.uncheckedCast(DomGlobal.window);
	}

}

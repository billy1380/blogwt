//
//  ElementAnimation.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 19 Jul 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.animation.element;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class ElementAnimation extends Animation {
	@FunctionalInterface
	public interface Complete {
		void on (Element e);
	}

	public Element e;
	public Complete c;
	@Override
	protected void onComplete () {
		super.onComplete();

		if (c != null) {
			c.on(e);
		}
	}

	public ElementAnimation e (Element value) {
		e = value;
		return this;
	}

	public ElementAnimation c (Complete value) {
		c = value;
		return this;
	}
}

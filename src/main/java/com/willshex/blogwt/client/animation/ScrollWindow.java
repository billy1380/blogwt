//
//  ScrollWindow.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 28 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.animation;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.user.client.Window;

/**
 * @author William Shakour (billy1380)
 *
 */
public class ScrollWindow extends Animation {
	private Easing easing;
	private double startLeft;
	private double distanceLeft;
	private double startTop;
	private double distanceTop;

	public ScrollWindow (int left, int top, Easing easing) {
		this.easing = easing;
		startLeft = Window.getScrollLeft();
		startTop = Window.getScrollTop();
		this.distanceLeft = left - startLeft;
		this.distanceTop = top - startTop;
	}

	public ScrollWindow (int top, Easing easing) {
		this(Window.getScrollLeft(), top, easing);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.animation.client.Animation#onUpdate(double) */
	@Override
	protected void onUpdate (double progress) {
		Window.scrollTo(
				(int) (startLeft + (easing.interpolate(progress) * distanceLeft)),
				(int) (startTop + (easing.interpolate(progress) * distanceTop)));
	}
}

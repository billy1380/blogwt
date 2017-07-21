//
//  FadeOut.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 19 Jul 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.animation.element;

import com.willshex.blogwt.client.animation.gdx.math.Interpolation;

/**
 * @author William Shakour (billy1380)
 *
 */
public class FadeOut extends ElementAnimation {
	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.animation.client.Animation#onUpdate(double) */
	@Override
	protected void onUpdate (double progress) {
		e.getStyle()
				.setOpacity(1 - Interpolation.fade.apply((float) progress));
	}

}

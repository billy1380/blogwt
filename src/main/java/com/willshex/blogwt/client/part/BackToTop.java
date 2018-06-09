//
//  BackToTop.java
//  com.willshex.codegen
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.animation.ScrollWindow;
import com.willshex.blogwt.client.animation.element.ElementAnimation;
import com.willshex.blogwt.client.animation.element.ElementAnimation.Complete;
import com.willshex.blogwt.client.animation.element.FadeIn;
import com.willshex.blogwt.client.animation.element.FadeOut;
import com.willshex.blogwt.client.animation.gdx.math.Interpolation;
import com.willshex.blogwt.client.gwt.RegisteringComposite;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BackToTop extends RegisteringComposite {

	private static final FadeIn FADE_IN = new FadeIn();
	private static final FadeOut FADE_OUT = new FadeOut();

	private static BackToTopUiBinder uiBinder = GWT
			.create(BackToTopUiBinder.class);

	interface BackToTopUiBinder extends UiBinder<Widget, BackToTop> {}

	@UiField Button btnBackToTop;

	private Timer toggleTop = new Timer() {

		@Override
		public void run () {
			if (shouldShow()) {
				fadeIn(btnBackToTop, 300, BackToTop.this::fadeInComplete);
			} else {
				fadeOut(btnBackToTop, 300, BackToTop.this::fadeOutComplete);
			}
		}
	};

	private void fadeInComplete (Element e) {
		e.getStyle().setOpacity(1.0);
	}

	private void fadeOutComplete (Element e) {
		e.getStyle().setOpacity(0.0);
		e.getStyle().setDisplay(Display.NONE);
	}

	public BackToTop () {
		initWidget(uiBinder.createAndBindUi(this));
		btnBackToTop.getElement().getStyle().setOpacity(0.0);
	}

	protected void fadeOut (Button button, float duration,
			Complete onComplete) {
		if (isOpacity(button, 1)) {
			runAnimation(FADE_OUT, button, duration, onComplete);
		}
	}

	protected void fadeIn (Button button, float duration, Complete onComplete) {
		if (isOpacity(button, 0)) {
			button.getElement().getStyle().clearDisplay();
			runAnimation(FADE_IN, button, duration, onComplete);
		}
	}

	private boolean isOpacity (Button button, float i) {
		return Float.valueOf(button.getElement().getStyle().getOpacity()) == i;
	}

	/**
	 * @param fadeOut
	 * @param button
	 * @param duration
	 * @param onComplete
	 */
	private void runAnimation (ElementAnimation a, Button button,
			float duration, Complete onComplete) {
		a.e(button.getElement()).c(onComplete).run((int) duration);
	}

	@UiHandler("btnBackToTop")
	void onBtnBackToTopClicked (ClickEvent e) {
		go();
	}

	public static void go () {
		new ScrollWindow(0, Interpolation.pow4).run(300);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		register(Window.addWindowScrollHandler(event -> {
			toggleTop.cancel();
			toggleTop.schedule(150);
		}));

		if (shouldShow()) {
			fadeInComplete(btnBackToTop.getElement());
		} else {
			fadeOutComplete(btnBackToTop.getElement());
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		super.onDetach();

		toggleTop.cancel();
	}

	private boolean shouldShow () {
		return Window.getScrollTop() > 200;
	}
}

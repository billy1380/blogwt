//
//  BackToTop.java
//  com.willshex.codegen
//
//  Created by William Shakour (billy1380) on 14 May 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.EasingCurve;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.animation.ScrollWindow;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BackToTop extends Composite {

	private static BackToTopUiBinder uiBinder = GWT
			.create(BackToTopUiBinder.class);

	interface BackToTopUiBinder extends UiBinder<Widget, BackToTop> {}

	@UiField Button btnBackToTop;
	private HandlerRegistration registration;
	private Timer toggleTop = new Timer() {

		@Override
		public void run () {
			if (shouldShow()) {
				$(btnBackToTop).fadeIn(300, fadeInComplete);
			} else {
				$(btnBackToTop).fadeOut(300, fadeOutComplete);
			}
		}
	};

	private final Function fadeInComplete = new Function() {
		public void f () {
			btnBackToTop.getElement().getStyle().setOpacity(1.0);
		}
	};

	private final Function fadeOutComplete = new Function() {
		public void f () {
			btnBackToTop.getElement().getStyle().setOpacity(0.0);
		}
	};

	public BackToTop () {
		initWidget(uiBinder.createAndBindUi(this));

		btnBackToTop
				.getElement()
				.setInnerHTML(
						SafeHtmlUtils
								.fromTrustedString(
										"<span class=\"glyphicon glyphicon-arrow-up aria-hidden=\"true\"></span> ")
								.asString()
								+ btnBackToTop.getText());

		btnBackToTop.getElement().getStyle().setOpacity(0.0);
	}

	@UiHandler("btnBackToTop")
	void onBtnBackToTopClicked (ClickEvent e) {
		new ScrollWindow(0, EasingCurve.easeInOutQuad).run(300);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		registration = Window.addWindowScrollHandler(new ScrollHandler() {

			@Override
			public void onWindowScroll (ScrollEvent event) {
				toggleTop.cancel();
				toggleTop.schedule(150);
			}
		});

		if (shouldShow()) {
			fadeInComplete.f();
		} else {
			fadeOutComplete.f();
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		super.onDetach();

		if (registration != null) {
			registration.removeHandler();
		}

		toggleTop.cancel();
	}

	private boolean shouldShow () {
		return Window.getScrollTop() > 200;
	}
}

//
//  CollapseButton.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 9 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CollapseButton extends Composite {

	private static CollapseButtonUiBinder uiBinder = GWT
			.create(CollapseButtonUiBinder.class);

	interface CollapseButtonUiBinder extends UiBinder<Widget, CollapseButton> {}

	private static final int TRANSITION_DURATION = 350;

	@UiField Button btnTrigger;
	private HTMLPanel pnlTarget = null;
	private boolean collapsed = true;
	private boolean transitioning = false;

	private Timer complete = new Timer() {
		public void run () {
			if (collapsed) {
				show();
			} else {
				hide();
			}

			collapsed = !collapsed;
		}

		private void hide () {
			transitioning = false;
			pnlTarget.removeStyleName("collapsing");
			pnlTarget.addStyleName("collapse");
		}

		private void show () {
			pnlTarget.removeStyleName("collapsing");
			pnlTarget.addStyleName("collapse in");
			transitioning = false;
		}
	};

	public CollapseButton () {
		initWidget(uiBinder.createAndBindUi(this));

		btnTrigger
				.getElement()
				.setInnerHTML(
						"<span class=\"sr-only\">Toggle navigation</span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span>");
	}

	/**
	 * @param target
	 */
	public void setTarget (HTMLPanel target) {
		this.pnlTarget = target;
		this.pnlTarget.addStyleName("collapse");
	}

	@UiHandler("btnTrigger")
	void onBtnTriggerClicked (ClickEvent event) {
		toggle();
	}

	public void toggle () {
		if (collapsed) {
			show();
		} else {
			hide();
		}
	}

	public void show () {
		if (!transitioning && collapsed) {
			pnlTarget.removeStyleName("collapse");
			pnlTarget.addStyleName("collapsing");

			btnTrigger.removeStyleName("collapsed");

			transitioning = true;

			complete.schedule(TRANSITION_DURATION);
		}
	}

	public void hide () {
		if (!transitioning && !collapsed) {
			pnlTarget.addStyleName("collapsing");
			pnlTarget.removeStyleName("collapse in");

			btnTrigger.addStyleName("collapsed");

			transitioning = true;

			complete.schedule(TRANSITION_DURATION);
		}
	}
}

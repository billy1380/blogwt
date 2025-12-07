//
//  CollapseButton.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 9 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.gwt.RegisteringComposite;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CollapseButton extends RegisteringComposite {

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
			pnlTarget.getElement().getStyle().clearHeight();
		}

		private void show () {
			pnlTarget.removeStyleName("collapsing");
			pnlTarget.addStyleName("collapse in");
			transitioning = false;

		}
	};

	public CollapseButton () {
		initWidget(uiBinder.createAndBindUi(this));
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

			Scheduler.get().scheduleDeferred( () -> {
				Element el = pnlTarget.getElement().getFirstChildElement();
				double height = (double) (el.getScrollHeight() + 9.0);
				while ((el = el.getNextSiblingElement()) != null) {
					height += (double) el.getScrollHeight() + 15.0;
				}

				pnlTarget.getElement().getStyle().setHeight(height, Unit.PX);
			});

			transitioning = true;

			complete.schedule(TRANSITION_DURATION);
		}
	}

	public void hide () {
		if (!transitioning && !collapsed) {
			pnlTarget.addStyleName("collapsing");
			pnlTarget.removeStyleName("collapse in");

			btnTrigger.addStyleName("collapsed");

			Scheduler.get().scheduleDeferred( () -> {
				pnlTarget.getElement().getStyle().setHeight(1.0, Unit.PX);
			});

			transitioning = true;

			complete.schedule(TRANSITION_DURATION);
		}
	}
	@Override
	protected void onAttach () {
		super.onAttach();

		register(Window.addResizeHandler( (e) -> {
			if (e.getWidth() > 768) {
				transitioning = false;
				collapsed = true;
				btnTrigger.addStyleName("collapsed");
				pnlTarget.removeStyleName("collapsing");
				pnlTarget.removeStyleName("collapse in");
				pnlTarget.addStyleName("collapse");
				pnlTarget.getElement().getStyle().clearHeight();
			}
		}));
	}
}

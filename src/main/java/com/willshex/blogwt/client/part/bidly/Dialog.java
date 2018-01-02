//
//  Dialog.java
//  bidly
//
//  Created by William Shakour (billy1380) on 2 Jan 2017.
//  Copyright © 2017 WillShex Ltd. All rights reserved.
//
package com.willshex.blogwt.client.part.bidly;

import java.util.function.Consumer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.willshex.blogwt.client.gwt.RegisteringComposite;

/**
 * @author William Shakour (billy1380)
 *
 */
public abstract class Dialog extends RegisteringComposite {

	protected HTMLPanel modalBackDrop = new HTMLPanel("");

	private String display;
	private Consumer<Integer> closed;
	private boolean closing = false;

	private Timer resizeTimer = new Timer() {
		@Override
		public void run () {
			resize();
		}
	};

	private void resize () {
		modalBackDrop.getElement().getStyle()
				.setHeight(getElement().getClientHeight(), Unit.PX);
	}

	public void show () {
		closing = false;
		display = getElement().getStyle().getDisplay();
		getElement().getStyle().setDisplay(Display.BLOCK);
		RootPanel.get().addStyleName("modal-open");

		modalBackDrop.setStyleName("fade");
		modalBackDrop.addStyleName("modal-backdrop");

		Document.get().getBody().appendChild(modalBackDrop.getElement());

		resizeTimer.cancel();
		resizeTimer.schedule(0);

		register(Window.addResizeHandler(event -> {
			resizeTimer.cancel();
			resizeTimer.schedule(150);
		}));

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute () {
				addStyleName("in");
				modalBackDrop.addStyleName("in");
			}
		});
	}

	public void hide (int result) {
		if (!closing) {
			closing = true;

			removeStyleName("in");
			modalBackDrop.removeStyleName("in");

			Scheduler.get().scheduleFixedDelay( () -> {
				getElement().getStyle().setProperty("display", display);
				RootPanel.get().removeStyleName("modal-open");
				modalBackDrop.getElement().removeFromParent();

				unregisterAll();

				if (closed != null) {
					Scheduler.get().scheduleDeferred(
							() -> closed.accept(Integer.valueOf(result)));

				}

				return false;
			}, 150);
		}
	}

	public void hide () {
		hide(0);
	}

	@Override
	protected void onDetach () {
		hide();
		super.onDetach();
	}

	public Dialog closed (Consumer<Integer> closed) {
		this.closed = closed;
		return this;
	}

	public Dialog show (Consumer<Integer> closed) {
		show();
		return closed(closed);
	}
}

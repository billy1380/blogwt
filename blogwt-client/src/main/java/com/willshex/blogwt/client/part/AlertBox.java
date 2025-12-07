//
//  AlertBox.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 26 Nov 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.Resources;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AlertBox extends Composite implements HasCloseHandlers<AlertBox> {

	private static AlertBoxUiBinder uiBinder = GWT
			.create(AlertBoxUiBinder.class);

	interface AlertBoxUiBinder extends UiBinder<Widget, AlertBox> {}

	public enum AlertBoxType {
		SuccessAlertBoxType,
		InfoAlertBoxType,
		WarningAlertBoxType,
		DangerAlertBoxType
	}

	private AlertBoxType type;

	@UiField SpanElement elText;
	@UiField SpanElement elDetail;
	@UiField Button btnClose;
	@UiField Image imgSpinner;
	private Timer timer;

	public AlertBox () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCanDismiss (boolean value) {
		if (value != this.getCanDismiss()) {
			if (value) {
				getWidget().addStyleName("alert-dismissible");
			} else {
				getWidget().removeStyleName("alert-dismissible");
			}
		}

		btnClose.setVisible(value);
	}

	public boolean getCanDismiss () {
		return btnClose.isVisible();
	}

	public void setType (AlertBoxType type) {
		if (this.type != null) {
			switch (this.type) {
			case DangerAlertBoxType:
				getWidget().removeStyleName("alert-danger");
				break;
			case InfoAlertBoxType:
				getWidget().removeStyleName("alert-info");
				break;
			case SuccessAlertBoxType:
				getWidget().removeStyleName("alert-success");
				break;
			case WarningAlertBoxType:
				getWidget().removeStyleName("alert-warning");
				break;
			}
		}

		this.type = type;

		switch (type) {
		case DangerAlertBoxType:
			getWidget().addStyleName("alert-danger");
			imgSpinner.setResource(Resources.RES.dangerLoader());
			break;
		case InfoAlertBoxType:
			getWidget().addStyleName("alert-info");
			imgSpinner.setResource(Resources.RES.infoLoader());
			break;
		case SuccessAlertBoxType:
			getWidget().addStyleName("alert-success");
			imgSpinner.setResource(Resources.RES.successLoader());
			break;
		case WarningAlertBoxType:
			getWidget().addStyleName("alert-warning");
			imgSpinner.setResource(Resources.RES.warningLoader());
			break;
		}
	}

	public AlertBoxType getType () {
		return type;
	}

	public void setLoading (boolean loading) {
		imgSpinner.setVisible(loading);
	}

	public boolean getLoading () {
		return imgSpinner.isVisible();
	}

	public void setText (String text) {
		elText.setInnerText(text);
	}

	public String getText () {
		return elText.getInnerText();
	}

	public void setDetail (String detail) {
		elDetail.setInnerText(detail);
	}

	public String getDetail () {
		return elDetail.getInnerText();
	}

	@UiHandler("btnClose")
	void onCloseClicked (ClickEvent e) {
		setVisible(false);

		CloseEvent.fire(this, this, false);
	}

	public void dismiss () {
		setVisible(false);

		CloseEvent.fire(this, this, true);
	}
	@Override
	public HandlerRegistration addCloseHandler (CloseHandler<AlertBox> handler) {
		return this.addHandler(handler, CloseEvent.getType());
	}

	public void startAutoRemoveTimer () {
		startAutoRemoveTimer(2000);
	}

	public void startAutoRemoveTimer (int delay) {
		if (timer == null) {
			timer = new Timer() {

				@Override
				public void run () {
					dismiss();
				}
			};
		} else {
			timer.cancel();
		}

		timer.schedule(delay);
	}
	@Override
	public void setVisible (boolean visible) {
		super.setVisible(visible);

		if (visible) {
			removeStyleName("hidden");
		} else {
			addStyleName("hidden");
		}
	}

}

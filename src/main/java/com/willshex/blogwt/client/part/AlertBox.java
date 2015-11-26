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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

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

	public AlertBox () {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setCanDismiss (boolean value) {
		btnClose.setVisible(value);
	}

	public boolean getCanDismiss () {
		return btnClose.isVisible();
	}

	public void setType (AlertBoxType type) {
		this.type = type;
	}

	public AlertBoxType getType () {
		return type;
	}

	public void setLoading (boolean loading) {
		imgSpinner.setVisible(loading);
		if (loading) {
			setType(AlertBoxType.InfoAlertBoxType);
		}
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

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.HasCloseHandlers#addCloseHandler(
	 * com.google.gwt.event.logical.shared.CloseHandler) */
	@Override
	public HandlerRegistration addCloseHandler (CloseHandler<AlertBox> handler) {
		return this.addHandler(handler, CloseEvent.getType());
	}

}

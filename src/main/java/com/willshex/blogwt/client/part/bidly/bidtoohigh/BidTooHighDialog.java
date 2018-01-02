//
//  About.java
//  dollar-challenge-client
//
//  Created by William Shakour (billy1380) on 15 Sep 2015.
//  Copyright © 2015 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.bidly.bidtoohigh;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.part.bidly.Dialog;
import com.willshex.blogwt.client.part.datefield.DateField;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class BidTooHighDialog extends Dialog {

	private static BidTooHighDialogUiBinder uiBinder = GWT
			.create(BidTooHighDialogUiBinder.class);

	interface BidTooHighDialogUiBinder
			extends UiBinder<Widget, BidTooHighDialog> {}

	@UiField Button btnClose;

	@UiField TextBox txtBid;
	@UiField ListBox cboDeliveryDate;
	@UiField DateField ctlExpiryDate;

	@UiField Button btnUpdate;

	public BidTooHighDialog () {
		initWidget(uiBinder.createAndBindUi(this));

		UiHelper.addPlaceholder(txtBid, "e.g. 1000");

		cboDeliveryDate.addItem("1 week", "1w");
		cboDeliveryDate.addItem("1 month", "1m");
		cboDeliveryDate.addItem("3 months", "3m");

		ctlExpiryDate.setFormat("d MMM yyyy");
		disablePast(ctlExpiryDate.ctlDate.getDatePicker());
	}

	void disablePast (DatePicker picker) {
		picker.addShowRangeHandler(e -> {
			Date start = e.getStart(), end = e.getEnd(),
					now = DateTimeHelper.now();
			Date current = CalendarUtil.copyDate(start);

			while (current.before(end)) {
				picker.setTransientEnabledOnDates(current.after(now), current);
				CalendarUtil.addDaysToDate(current, 1);
			}
		});
	}

	@UiHandler("btnClose")
	void onCloseClicked (ClickEvent ce) {
		hide();
	}

	@UiHandler("btnUpdate")
	void onUpdateClicked (ClickEvent ce) {
		hide(1);
	}

}

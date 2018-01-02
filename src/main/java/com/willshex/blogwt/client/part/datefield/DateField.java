//
//  DateField.java
//  blogwt
//
//  Created by William Shakour (billy1380) on 19 Sep 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
//
package com.willshex.blogwt.client.part.datefield;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.willshex.blogwt.client.helper.UiHelper;
import com.willshex.blogwt.client.part.BootstrapGwtDatePicker;
import com.willshex.blogwt.shared.helper.DateTimeHelper;

/**
 * @author William Shakour (billy1380)
 *
 */
public class DateField extends Composite
		implements HasValueChangeHandlers<Date>, HasValue<Date> {

	private static DateFieldUiBinder uiBinder = GWT
			.create(DateFieldUiBinder.class);

	interface DateFieldUiBinder extends UiBinder<Widget, DateField> {}

	@UiField public DateBox ctlDate;
	@UiField Element elStatusIcon;

	private static final String CALENDAR = "glyphicon glyphicon-calendar";
	private static final String CLOSE = "glyphicon glyphicon-remove";

	public DateField () {
		initWidget(uiBinder.createAndBindUi(this));

		BootstrapGwtDatePicker.INSTANCE.styles().ensureInjected();
		showIcon(CALENDAR);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		super.onAttach();

		this.removeStyleName("input-group");
		this.addStyleName("input-group");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#
	 * addValueChangeHandler(com.google.gwt.event.logical.shared.
	 * ValueChangeHandler) */
	@Override
	public HandlerRegistration addValueChangeHandler (
			ValueChangeHandler<Date> handler) {
		return ctlDate.addValueChangeHandler(handler);
	}

	public void setPlaceholder (String s) {
		UiHelper.addPlaceholder(ctlDate.getTextBox(), s);
	}

	/**
	 * @param pattern
	 */
	public void setFormat (String pattern) {
		ctlDate.setFormat(new DefaultFormat(DateTimeFormat.getFormat(pattern)));
		UiHelper.addPlaceholder(ctlDate.getTextBox(), "e.g. "
				+ ctlDate.getFormat().format(ctlDate, DateTimeHelper.now()));
	}

	/**
	 * @return
	 */
	public boolean isEmpty () {
		return ctlDate.getTextBox().getValue().trim().isEmpty();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#getValue() */
	@Override
	public Date getValue () {
		return ctlDate.getValue();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object) */
	@Override
	public void setValue (Date value) {
		ctlDate.setValue(value);

		if (value == null) {
			showIcon(CALENDAR);
		} else {
			showIcon(CLOSE);
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object,
	 * boolean) */
	@Override
	public void setValue (Date value, boolean fireEvents) {
		ctlDate.setValue(value, fireEvents);

		if (!fireEvents) {
			if (value == null) {
				showIcon(CALENDAR);
			} else {
				showIcon(CLOSE);
			}
		}
	}

	@UiHandler("btnStatus")
	void onStatusClicked (ClickEvent ce) {
		if (ctlDate.getValue() == null) {
			ctlDate.showDatePicker();
			ctlDate.setFocus(true);
		} else {
			ctlDate.setValue(null, true);
			showIcon(CALENDAR);
		}
	}

	@UiHandler("ctlDate")
	void onValueChanged (ValueChangeEvent<Date> vce) {
		if (vce.getValue() == null) {
			showIcon(CALENDAR);
		} else {
			showIcon(CLOSE);
		}
	}

	private void showIcon (String style) {
		elStatusIcon.removeClassName(CALENDAR);
		elStatusIcon.removeClassName(CLOSE);
		elStatusIcon.addClassName(style);
	}

}

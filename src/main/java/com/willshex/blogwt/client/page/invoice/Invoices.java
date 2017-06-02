//
//  Invoices.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.event.SetInvoiceStatusEventHandler;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.invoice.InvoiceController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceStatusType;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusResponse;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Invoices extends Page implements SetInvoiceStatusEventHandler, NavigationChangedEventHandler {

	private List<HandlerRegistration> registration;

	private static InvoicesUiBinder uiBinder = GWT.create(InvoicesUiBinder.class);

	interface InvoicesUiBinder extends UiBinder<Widget, Invoices> {}

	@UiField(provided = true) CellTable<Invoice> invoiceTable = new CellTable<Invoice>(10, BootstrapGwtCellTable.INSTANCE);
	@UiField SimplePager simplePager;

	private TextColumn<Invoice> customerField;
	private Column<Invoice, Date> dateField;
	private Column<Invoice, SafeHtml> noField;
	private Column<Invoice, String> statusField;
	private Column<Invoice, SafeHtml> amountField;
	private Cell<Date> dateCellPrototype = new DateCell(DateTimeFormat.getFormat("dd MMM yyyy"));

	public Invoices() {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		InvoiceController.get().addDataDisplay(invoiceTable);
		invoiceTable.setEmptyTableWidget(new HTML("No invoices!"));
		simplePager.setDisplay(invoiceTable);
	}

	private void createColumns() {
		dateField = new Column<Invoice, Date>(dateCellPrototype) {
			@Override
			public Date getValue(Invoice object) {
				return object.date;
			}
		};
		invoiceTable.addColumn(dateField, "Date");

		noField = new Column<Invoice, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Invoice object) {
				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml("<a target=\"_blank\" href=\"view/?invoice=" + object.name + "\">"
						+ object.name + "</a>");
			}
		};
		invoiceTable.addColumn(noField, "Name");

		customerField = new TextColumn<Invoice>() {
			@Override
			public String getValue(Invoice object) {
				return object.customer.name;
			}
		};
		invoiceTable.addColumn(customerField, "Customer");

		List<String> statae = new ArrayList<String>();
		for (InvoiceStatusType status : InvoiceStatusType.values()) {
			statae.add(status.toString());
		}

		statusField = new Column<Invoice, String>(new SelectionCell(statae)) {
			@Override
			public String getValue(Invoice object) {
				return object.status.toString();
			}
		};

		statusField.setFieldUpdater(new FieldUpdater<Invoice, String>() {

			@Override
			public void update(int index, Invoice object, String value) {
				Invoice invoice = new Invoice();
				invoice.name = object.name;
				invoice.status = InvoiceStatusType.fromString(value);

				InvoiceController.get().updateInvoice(invoice);
			}
		});
		invoiceTable.addColumn(statusField, "Status");

		amountField = new Column<Invoice, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Invoice object) {
				float amount = object.totalPrice == null ? 0.0f : object.totalPrice.floatValue();
				amount += ((float) amount * (object.tax.floatValue() / 100.0f)) - object.deposit.intValue();
				amount /= 100.0f;
				String currency = "&pound;";

				if (object.currency.symbol != null && !"".equals(object.currency.symbol)) {
					currency = object.currency.symbol;
				}

				return new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(currency + NumberFormat.getFormat("#.00").format(amount));
			}
		};

		amountField.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		invoiceTable.addColumn(amountField, "Amount");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach()
	 */
	@Override
	protected void onAttach() {
		if (registration == null) {
			registration = new ArrayList<HandlerRegistration>();
		}

		registration.add(DefaultEventBus.get().addHandlerToSource(NavigationChangedEventHandler.TYPE, NavigationController.get(), this));
		registration.add(DefaultEventBus.get().addHandlerToSource(SetInvoiceStatusEventHandler.TYPE, InvoiceController.get(), this));

		super.onAttach();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach()
	 */
	@Override
	protected void onDetach() {
		for (HandlerRegistration handlerRegistration : registration) {
			handlerRegistration.removeHandler();
		}

		super.onDetach();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.shared.api.core.call.event.SetInvoiceStatusEventHandler#setInvoiceStatusSuccess(com.spacehopperstudios.quickinvoice
	 * .shared.api.core.call.SetInvoiceStatusRequest, com.spacehopperstudios.quickinvoice.shared.api.core.call.SetInvoiceStatusResponse)
	 */
	@Override
	public void setInvoiceStatusSuccess(SetInvoiceStatusRequest input, SetInvoiceStatusResponse output) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.spacehopperstudios.quickinvoice.shared.api.core.call.event.SetInvoiceStatusEventHandler#setInvoiceStatusFailure(com.spacehopperstudios.quickinvoice
	 * .shared.api.core.call.SetInvoiceStatusRequest, java.lang.Throwable)
	 */
	@Override
	public void setInvoiceStatusFailure(SetInvoiceStatusRequest input, Throwable caught) {
		invoiceTable.redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.quickinvoice.client.handler.NavigationChangedEventHandler#navigationChanged(com.willshex.quickinvoice.client.controller.NavigationController
	 * .Stack, com.willshex.quickinvoice.client.controller.NavigationController.Stack)
	 */
	@Override
	public void navigationChanged(Stack previous, Stack current) {
		String action = current.getAction();
		if (action != null) {
			String[] parts = action.split("=");

			if (parts != null && parts.length > 1) {
				if ("action".equals(parts[0]) && "refresh".equals(parts[1])) {
					invoiceTable.setVisibleRangeAndClearData(invoiceTable.getVisibleRange(), true);
				}
			}
		}
	}

}

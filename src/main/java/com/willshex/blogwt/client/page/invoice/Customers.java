//
//  Customers.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 20 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.controller.NavigationController;
import com.willshex.blogwt.client.controller.invoice.CustomerController;
import com.willshex.blogwt.client.event.NavigationChangedEventHandler;
import com.willshex.blogwt.client.page.Page;
import com.willshex.blogwt.client.part.BootstrapGwtCellTable;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.shared.page.Stack;

/**
 * @author William Shakour (billy1380)
 *
 */
public class Customers extends Page implements NavigationChangedEventHandler {

	private HandlerRegistration registration;

	private static CustomersUiBinder uiBinder = GWT
			.create(CustomersUiBinder.class);

	interface CustomersUiBinder extends UiBinder<Widget, Customers> {}

	@UiField(provided = true) CellTable<Customer> customerTable = new CellTable<Customer>(
			PagerHelper.DEFAULT_COUNT.intValue(),
			BootstrapGwtCellTable.INSTANCE);
	// @UiField SimplePager simplePager;

	private TextColumn<Customer> addressColumn;
	private TextColumn<Customer> nameColumn;
	private TextColumn<Customer> codeColumn;
	private TextColumn<Customer> emailColumn;
	private TextColumn<Customer> descriptionColumn;
	private TextColumn<Customer> phoneColumn;
	private TextColumn<Customer> invoiceCountColumn;
	private TextColumn<Customer> outstandingCountColumn;

	public Customers () {
		initWidget(uiBinder.createAndBindUi(this));

		createColumns();

		CustomerController.get().addDataDisplay(customerTable);
		customerTable.setEmptyTableWidget(new HTML("No clients!"));
		// simplePager.setDisplay(customerTable);
	}

	public void createColumns () {
		addressColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.address == null ? "none"
						: object.address.postalCode;
			}
		};

		nameColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.name;
			}
		};

		codeColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.code;
			}
		};

		emailColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return (object.mainEmail == null ? "none" : object.mainEmail)
						+ (object.secondaryEmail == null ? ""
								: ", " + object.secondaryEmail);
			}
		};

		descriptionColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.description;
			}
		};

		phoneColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.phone;
			}
		};

		invoiceCountColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.invoiceCount.toString();
			}
		};

		outstandingCountColumn = new TextColumn<Customer>() {

			@Override
			public String getValue (Customer object) {
				return object.outstandingCount.toString();
			}
		};

		customerTable.addColumn(codeColumn, "Code");
		customerTable.addColumn(nameColumn, "Name");
		customerTable.addColumn(outstandingCountColumn, "Outstanding");
		customerTable.addColumn(descriptionColumn, "Description");
		customerTable.addColumn(phoneColumn, "Phone");
		customerTable.addColumn(emailColumn, "Email");
		customerTable.addColumn(addressColumn, "Address");
		customerTable.addColumn(invoiceCountColumn, "Total");
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		registration = DefaultEventBus.get().addHandlerToSource(
				NavigationChangedEventHandler.TYPE, NavigationController.get(),
				this);

		super.onAttach();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onDetach() */
	@Override
	protected void onDetach () {
		registration.removeHandler();

		super.onDetach();
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.willshex.quickinvoice.client.handler.NavigationChangedEventHandler#
	 * navigationChanged(com.willshex.quickinvoice.client.controller.
	 * NavigationController .Stack,
	 * com.willshex.quickinvoice.client.controller.NavigationController.
	 * Stack) */
	@Override
	public void navigationChanged (Stack previous, Stack current) {
		String action = current.getAction();
		if (action != null) {
			String[] parts = action.split("=");

			if (parts != null && parts.length > 1) {
				if ("action".equals(parts[0]) && "refresh".equals(parts[1])) {
					customerTable.setVisibleRangeAndClearData(
							customerTable.getVisibleRange(), true);
				}
			}
		}
	}

}

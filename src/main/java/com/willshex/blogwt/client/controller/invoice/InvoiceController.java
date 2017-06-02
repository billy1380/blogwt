//
//  InvoiceController.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 21 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.controller.invoice;

import java.util.Collections;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.InvoiceService;
import com.willshex.blogwt.client.api.invoice.event.CreateInvoiceEventHandler.CreateInvoiceFailure;
import com.willshex.blogwt.client.api.invoice.event.CreateInvoiceEventHandler.CreateInvoiceSuccess;
import com.willshex.blogwt.client.api.invoice.event.GetInvoicesEventHandler.GetInvoicesFailure;
import com.willshex.blogwt.client.api.invoice.event.GetInvoicesEventHandler.GetInvoicesSuccess;
import com.willshex.blogwt.client.api.invoice.event.SetInvoiceStatusEventHandler.SetInvoiceStatusFailure;
import com.willshex.blogwt.client.api.invoice.event.SetInvoiceStatusEventHandler.SetInvoiceStatusSuccess;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.invoice.ApiHelper;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;
import com.willshex.blogwt.shared.api.datatype.invoice.InvoiceSortType;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceRequest;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceResponse;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetInvoicesResponse;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetInvoiceStatusResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class InvoiceController extends AsyncDataProvider<Invoice> {

	private static InvoiceController one = null;

	public static InvoiceController get () {
		if (one == null) {
			one = new InvoiceController();
		}
		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager()
			.sortBy(InvoiceSortType.InvoiceSortTypeDate.toString());

	public void updateInvoice (Invoice invoice) {
		InvoiceService service = ApiHelper.createInvoiceClient();

		final SetInvoiceStatusRequest input = ApiHelper
				.setAccessCode(new SetInvoiceStatusRequest())
				.status(invoice.status);
		input.session = SessionController.get().sessionForApiCall();

		(input.invoice = new Invoice()).name(invoice.name);

		service.setInvoiceStatus(input,
				new AsyncCallback<SetInvoiceStatusResponse>() {

					@Override
					public void onSuccess (SetInvoiceStatusResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {

				} else {}

						DefaultEventBus.get().fireEventFromSource(
								new SetInvoiceStatusSuccess(input, output),
								InvoiceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new SetInvoiceStatusFailure(input, caught),
								InvoiceController.this);
					}
				});
	}

	public void createInvoice (Invoice invoice) {
		InvoiceService service = ApiHelper.createInvoiceClient();

		final CreateInvoiceRequest input = ApiHelper
				.setAccessCode(new CreateInvoiceRequest());
		input.invoice = invoice;
		input.session = SessionController.get().sessionForApiCall();

		service.createInvoice(input,
				new AsyncCallback<CreateInvoiceResponse>() {

					@Override
					public void onSuccess (CreateInvoiceResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {} else {}

						DefaultEventBus.get().fireEventFromSource(
								new CreateInvoiceSuccess(input, output),
								InvoiceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new CreateInvoiceFailure(input, caught),
								InvoiceController.this);
					}
				});
	}

	private void fetchInvoices () {
		final GetInvoicesRequest input = ApiHelper
				.setAccessCode(new GetInvoicesRequest());
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		ApiHelper.createInvoiceClient().getInvoices(input,
				new AsyncCallback<GetInvoicesResponse>() {

					@Override
					public void onSuccess (GetInvoicesResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							if (output.invoices != null
									&& output.invoices.size() > 0) {
								pager = output.pager;
								updateRowCount(
										input.pager.count == null ? 0
												: input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count
														.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.invoices);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Invoice> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetInvoicesSuccess(input, output),
								InvoiceController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetInvoicesFailure(input, caught),
								InvoiceController.this);
					}

				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Invoice> display) {
		Range range = display.getVisibleRange();
		pager.start(Integer.valueOf(range.getStart()))
				.count(Integer.valueOf(range.getLength()));

		fetchInvoices();
	}

}

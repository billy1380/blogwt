//
//  CustomerController.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 24 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.controller.invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.InvoiceService;
import com.willshex.blogwt.client.api.invoice.event.AddCustomerEventHandler.AddCustomerFailure;
import com.willshex.blogwt.client.api.invoice.event.AddCustomerEventHandler.AddCustomerSuccess;
import com.willshex.blogwt.client.api.invoice.event.GetCustomersEventHandler.GetCustomersFailure;
import com.willshex.blogwt.client.api.invoice.event.GetCustomersEventHandler.GetCustomersSuccess;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.invoice.ApiHelper;
import com.willshex.blogwt.client.helper.invoice.internal.LookupCache;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;
import com.willshex.blogwt.shared.api.datatype.invoice.CustomerSortType;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerRequest;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerResponse;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCustomersResponse;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class CustomerController extends AsyncDataProvider<Customer> {

	private static CustomerController one = null;

	public static CustomerController get () {
		if (one == null) {
			one = new CustomerController();
		}

		return one;
	}

	private Pager pager = PagerHelper.createDefaultPager()
			.sortBy(CustomerSortType.CustomerSortTypeName.toString())
			.sortDirection(SortDirectionType.SortDirectionTypeAscending);

	private CustomerController () {
		String customersJson = customers();

		if (customersJson != null) {
			JsonArray jsonCustomers = (new JsonParser()).parse(customersJson)
					.getAsJsonArray();
			Customer item = null;
			for (int i = 0; i < jsonCustomers.size(); i++) {
				if (jsonCustomers.get(i).isJsonObject()) {
					(item = new Customer())
							.fromJson(jsonCustomers.get(i).getAsJsonObject());
					LookupCache.customers.put(item.code, item);
				}
			}
		}
	}

	public void fetchCustomers () {
		final GetCustomersRequest input = new GetCustomersRequest();
		input.accessCode = ApiHelper.ACCESS_CODE;
		input.pager = pager;
		input.session = SessionController.get().sessionForApiCall();

		ApiHelper.createInvoiceClient().getCustomers(input,
				new AsyncCallback<GetCustomersResponse>() {

					@Override
					public void onSuccess (GetCustomersResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {
							pager = output.pager;

							if (output.customers != null
									&& output.customers.size() > 0) {
								for (Customer customer : output.customers) {
									LookupCache.customers.put(customer.code,
											customer);
								}

								updateRowCount(input.pager.count == null ? 0
										: input.pager.start.intValue()
												+ input.pager.count.intValue(),
										input.pager.count == null
												|| input.pager.count
														.intValue() == 0);
								updateRowData(input.pager.start.intValue(),
										output.customers);
							} else {
								updateRowCount(input.pager.start.intValue(),
										true);
								updateRowData(input.pager.start.intValue(),
										Collections.<Customer> emptyList());
							}
						}

						DefaultEventBus.get().fireEventFromSource(
								new GetCustomersSuccess(null, output),
								CustomerController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetCustomersFailure(null, caught),
								CustomerController.this);
					}
				});
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.AbstractDataProvider#onRangeChanged(com.google
	 * .gwt.view.client.HasData) */
	@Override
	protected void onRangeChanged (HasData<Customer> display) {
		boolean fetch = false;

		String customersJson = customers();
		if (customersJson != null) {
			JsonArray jsonArray = new JsonParser().parse(customersJson)
					.getAsJsonArray();

			if (jsonArray.size() != 0) {
				List<Customer> customers = new ArrayList<Customer>();

				Customer customer;
				for (JsonElement customerJson : jsonArray) {
					customer = new Customer();
					customer.fromJson(customerJson.getAsJsonObject());
					customers.add(customer);
					LookupCache.customers.put(customer.code, customer);
				}

				updateRowData(0, customers);

				removeJsonCustomers();
			} else {
				fetch = true;
			}
		} else {
			fetch = true;
		}

		if (fetch) {
			Range range = display.getVisibleRange();
			pager.start(Integer.valueOf(range.getStart()))
					.count(Integer.valueOf(range.getLength()));

			fetchCustomers();
		}
	}

	public void addCustomer (Customer customer) {
		InvoiceService service = ApiHelper.createInvoiceClient();

		final AddCustomerRequest input = ApiHelper
				.setAccessCode(new AddCustomerRequest());
		input.customer = customer;
		input.session = SessionController.get().sessionForApiCall();

		service.addCustomer(input, new AsyncCallback<AddCustomerResponse>() {

			@Override
			public void onSuccess (AddCustomerResponse output) {
				if (output.status != StatusType.StatusTypeFailure) {

				} else {}

				DefaultEventBus.get().fireEventFromSource(
						new AddCustomerSuccess(input, output),
						CustomerController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new AddCustomerFailure(input, caught),
						CustomerController.this);
			}
		});
	}

	public void updateCustomer (Customer customer) {

	}

	private static native void removeJsonCustomers ()
	/*-{
	$wnd['customers'] = null;
	}-*/;

	private static native String customers ()
	/*-{
	return $wnd['customers'];
	}-*/;

}

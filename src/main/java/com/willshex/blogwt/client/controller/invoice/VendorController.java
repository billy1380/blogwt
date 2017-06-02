//
//  VendorController.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 25 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS LTD. All rights reserved.
//
package com.willshex.blogwt.client.controller.invoice;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.InvoiceService;
import com.willshex.blogwt.client.api.invoice.event.GetCurrentVendorEventHandler.GetCurrentVendorFailure;
import com.willshex.blogwt.client.api.invoice.event.GetCurrentVendorEventHandler.GetCurrentVendorSuccess;
import com.willshex.blogwt.client.api.invoice.event.SetupVendorEventHandler.SetupVendorFailure;
import com.willshex.blogwt.client.api.invoice.event.SetupVendorEventHandler.SetupVendorSuccess;
import com.willshex.blogwt.client.controller.SessionController;
import com.willshex.blogwt.client.helper.invoice.ApiHelper;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.GetCurrentVendorResponse;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorResponse;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class VendorController {

	private static VendorController one = null;
	private Vendor current;

	public static VendorController get () {
		if (one == null) {
			one = new VendorController();
		}
		return one;
	}

	/**
	 * 
	 */
	public VendorController () {
		String vendorJson = currentVendor();

		if (vendorJson != null) {
			(current = new Vendor()).fromJson(vendorJson);
		}
	}

	public Vendor current () {
		return current;
	}

	private void fetchCurrentVendor () {
		InvoiceService service = ApiHelper.createInvoiceClient();

		final GetCurrentVendorRequest input = ApiHelper
				.setAccessCode(new GetCurrentVendorRequest());
		input.session = SessionController.get().sessionForApiCall();

		service.getCurrentVendor(input,
				new AsyncCallback<GetCurrentVendorResponse>() {

					@Override
					public void onSuccess (GetCurrentVendorResponse output) {
						if (output.status == StatusType.StatusTypeSuccess) {} else {

				}

						DefaultEventBus.get().fireEventFromSource(
								new GetCurrentVendorSuccess(input, output),
								VendorController.this);
					}

					@Override
					public void onFailure (Throwable caught) {
						DefaultEventBus.get().fireEventFromSource(
								new GetCurrentVendorFailure(input, caught),
								VendorController.this);
					}
				});
	}

	public void setupVendor (Vendor vendor, User admin) {
		InvoiceService service = ApiHelper.createInvoiceClient();

		final SetupVendorRequest input = ApiHelper
				.setAccessCode(new SetupVendorRequest());
		input.vendor = vendor;
		input.admin = admin;

		service.setupVendor(input, new AsyncCallback<SetupVendorResponse>() {

			@Override
			public void onSuccess (SetupVendorResponse output) {
				if (output.status != StatusType.StatusTypeFailure) {

				} else {}

				DefaultEventBus.get().fireEventFromSource(
						new SetupVendorSuccess(input, output),
						VendorController.this);
			}

			@Override
			public void onFailure (Throwable caught) {
				DefaultEventBus.get().fireEventFromSource(
						new SetupVendorFailure(input, caught),
						VendorController.this);
			}
		});
	}

	private static native String currentVendor ()
	/*-{
	return $wnd['currentVendor'];
	}-*/;
}

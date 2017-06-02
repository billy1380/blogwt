//
//  AddCustomer.java
//  reckoning
//
//  Created by William Shakour (billy1380) on 26 Apr 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.event.AddCustomerEventHandler;
import com.willshex.blogwt.client.controller.invoice.CustomerController;
import com.willshex.blogwt.client.helper.invoice.PageTypeHelper;
import com.willshex.blogwt.client.page.invoice.wizard.AddAddressWizardPage;
import com.willshex.blogwt.client.page.invoice.wizard.AddCustomerWizardPage;
import com.willshex.blogwt.client.part.BootstrapGwtDatePicker;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.Customer;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerRequest;
import com.willshex.blogwt.shared.api.invoice.call.AddCustomerResponse;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

/**
 * @author William Shakour (billy1380)
 *
 */
public class AddCustomer extends WizardDialog
		implements AddCustomerEventHandler {

	private HandlerRegistration registration;

	/**
	 * 
	 */
	public AddCustomer () {
		super();

		BootstrapGwtDatePicker.INSTANCE.styles().ensureInjected();

		setPlan((new PagePlanBuilder()).addPage(new AddCustomerWizardPage())
				.addPage(new AddAddressWizardPage()).setName("Add Client")
				.addFinishedHandler(new PagePlanFinishedHandler() {

					@Override
					public void onfinished (List<WizardPage<?>> pages) {
						finish(pages);
					}

					@Override
					public void onCancelled () {}
				}).build());
	}

	/* (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onAttach() */
	@Override
	protected void onAttach () {
		registration = DefaultEventBus.get().addHandlerToSource(
				AddCustomerEventHandler.TYPE, CustomerController.get(), this);
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

	private void finish (List<WizardPage<?>> pages) {
		Customer customer = ((AddCustomerWizardPage) pages.get(0)).getData();
		customer.address = ((AddAddressWizardPage) pages.get(1)).getData();

		CustomerController.get().addCustomer(customer);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.shared.api.core.call.event.
	 * AddCustomerEventHandler#addCustomerSuccess(com.spacehopperstudios.
	 * quickinvoice.shared. api.core.call.AddCustomerRequest,
	 * com.spacehopperstudios.quickinvoice.shared.api.core.call.
	 * AddCustomerResponse) */
	@Override
	public void addCustomerSuccess (AddCustomerRequest input,
			AddCustomerResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageTypeHelper.show(PageType.CustomersPageType, "action=refresh");
		} else {
			// TODO: show error
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.shared.api.core.call.event.
	 * AddCustomerEventHandler#addCustomerFailure(com.spacehopperstudios.
	 * quickinvoice.shared. api.core.call.AddCustomerRequest,
	 * java.lang.Throwable) */
	@Override
	public void addCustomerFailure (AddCustomerRequest input,
			Throwable caught) {}

}

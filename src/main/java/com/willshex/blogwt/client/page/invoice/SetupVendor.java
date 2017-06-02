//
//  SetupVendor.java
//  reckoning
//
//  Created by William Shakour (billy1380) on 26 Apr 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.event.SetupVendorEventHandler;
import com.willshex.blogwt.client.controller.invoice.VendorController;
import com.willshex.blogwt.client.page.invoice.wizard.AddAddressWizardPage;
import com.willshex.blogwt.client.page.invoice.wizard.AddBankAccountWizardPage;
import com.willshex.blogwt.client.page.invoice.wizard.SetupVendorWizardPage;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.client.wizard.page.AddUserWizardPage;
import com.willshex.blogwt.shared.api.datatype.User;
import com.willshex.blogwt.shared.api.datatype.invoice.BankAccount;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorResponse;

/**
 * @author William Shakour (billy1380)
 *
 */
public class SetupVendor extends WizardDialog
		implements SetupVendorEventHandler {

	private HandlerRegistration registration;

	public SetupVendor () {
		super();

		setPlan((new PagePlanBuilder()).addPage(new SetupVendorWizardPage())
				.addPage(new AddAddressWizardPage())
				.addPage(new AddBankAccountWizardPage())
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
				SetupVendorEventHandler.TYPE, VendorController.get(), this);
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
		Vendor vendor = ((SetupVendorWizardPage) pages.get(0)).getData();

		vendor.address = ((AddAddressWizardPage) pages.get(1)).getData();
		vendor.accounts = new ArrayList<BankAccount>();
		vendor.accounts
				.add(((AddBankAccountWizardPage) pages.get(2)).getData());

		User admin = ((AddUserWizardPage) pages.get(3)).getData();

		VendorController.get().setupVendor(vendor, admin);
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.shared.api.core.call.event.
	 * SetupVendorEventHandler#setupVendorSuccess(com.spacehopperstudios.
	 * quickinvoice.shared. api.core.call.SetupVendorRequest,
	 * com.spacehopperstudios.quickinvoice.shared.api.core.call.
	 * SetupVendorResponse) */
	@Override
	public void setupVendorSuccess (SetupVendorRequest input,
			SetupVendorResponse output) {
		Window.Location.reload();
	}

	/* (non-Javadoc)
	 * 
	 * @see com.spacehopperstudios.quickinvoice.shared.api.core.call.event.
	 * SetupVendorEventHandler#setupVendorFailure(com.spacehopperstudios.
	 * quickinvoice.shared. api.core.call.SetupVendorRequest,
	 * java.lang.Throwable) */
	@Override
	public void setupVendorFailure (SetupVendorRequest input,
			Throwable caught) {
		// TODO: show errors
	}

}

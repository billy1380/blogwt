//
//  AddInvoice.java
//  quickinvoice
//
//  Created by William Shakour (billy1380) on 21 Dec 2014.
//  Copyright © 2014 SPACEHOPPER STUDIOS Ltd. All rights reserved.
//
package com.willshex.blogwt.client.page.invoice;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.willshex.blogwt.client.DefaultEventBus;
import com.willshex.blogwt.client.api.invoice.event.CreateInvoiceEventHandler;
import com.willshex.blogwt.client.controller.invoice.InvoiceController;
import com.willshex.blogwt.client.controller.invoice.VendorController;
import com.willshex.blogwt.client.helper.invoice.PageTypeHelper;
import com.willshex.blogwt.client.page.invoice.wizard.AddInvoiceItemWizardPage;
import com.willshex.blogwt.client.page.invoice.wizard.AddInvoiceWizardPage;
import com.willshex.blogwt.client.part.BootstrapGwtDatePicker;
import com.willshex.blogwt.client.wizard.PagePlan.PagePlanBuilder;
import com.willshex.blogwt.client.wizard.PagePlanFinishedHandler;
import com.willshex.blogwt.client.wizard.WizardDialog;
import com.willshex.blogwt.client.wizard.WizardPage;
import com.willshex.blogwt.shared.api.datatype.invoice.Invoice;
import com.willshex.blogwt.shared.api.datatype.invoice.Item;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceRequest;
import com.willshex.blogwt.shared.api.invoice.call.CreateInvoiceResponse;
import com.willshex.blogwt.shared.page.PageType;
import com.willshex.gson.web.service.shared.StatusType;

public class AddInvoice extends WizardDialog
		implements CreateInvoiceEventHandler {
	private HandlerRegistration registration;

	public AddInvoice () {
		super();

		BootstrapGwtDatePicker.INSTANCE.styles().ensureInjected();

		setPlan((new PagePlanBuilder()).addPage(new AddInvoiceWizardPage())
				.addPage(new AddInvoiceItemWizardPage()).setName("Add Invoice")
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
				CreateInvoiceEventHandler.TYPE, InvoiceController.get(), this);

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
		Invoice invoice = ((AddInvoiceWizardPage) pages.get(0)).getData();
		invoice.items = new ArrayList<Item>();

		int pageCount = pages.size();
		for (int i = 1; i < pageCount; i++) {
			invoice.items
					.add(((AddInvoiceItemWizardPage) pages.get(i)).getData());
		}

		invoice.account = VendorController.get().current().accounts.get(0);

		InvoiceController.get().createInvoice(invoice);
	}

	@Override
	public void createInvoiceSuccess (CreateInvoiceRequest input,
			CreateInvoiceResponse output) {
		if (output.status == StatusType.StatusTypeSuccess) {
			PageTypeHelper.show(PageType.InvoicesPageType, "action=refresh");
		} else {
			// TODO: show error
		}
	}

	@Override
	public void createInvoiceFailure (CreateInvoiceRequest input,
			Throwable caught) {}

}

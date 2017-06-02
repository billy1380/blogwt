// 
//  SetupVendorActionHandler.java
//  rekoning
// 
//  Created by William Shakour on June 2, 2017.
//  Copyright © 2017 WillShex Limited. All rights reserved.
// 
package com.willshex.blogwt.server.api.invoice.action;

import java.util.Arrays;
import java.util.logging.Logger;

import com.willshex.blogwt.server.api.ActionHandler;
import com.willshex.blogwt.server.service.invoice.address.AddressServiceProvider;
import com.willshex.blogwt.server.service.invoice.bankaccount.BankAccountServiceProvider;
import com.willshex.blogwt.server.service.invoice.bankaccount.IBankAccountService;
import com.willshex.blogwt.server.service.invoice.currency.CurrencyServiceProvider;
import com.willshex.blogwt.server.service.invoice.vendor.VendorServiceProvider;
import com.willshex.blogwt.server.service.resource.ResourceServiceProvider;
import com.willshex.blogwt.server.service.user.UserServiceProvider;
import com.willshex.blogwt.shared.api.datatype.invoice.BankAccount;
import com.willshex.blogwt.shared.api.datatype.invoice.Vendor;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorRequest;
import com.willshex.blogwt.shared.api.invoice.call.SetupVendorResponse;
import com.willshex.gson.web.service.server.ServiceException;

public final class SetupVendorActionHandler
		extends ActionHandler<SetupVendorRequest, SetupVendorResponse> {

	private static final Logger LOG = Logger
			.getLogger(SetupVendorActionHandler.class.getName());

	@Override
	public void handle (SetupVendorRequest input, SetupVendorResponse output)
			throws Exception {
		Vendor currentVendor = null;
		try {
			currentVendor = VendorServiceProvider.provide().getCurrentVendor();
		} catch (Exception e) {}

		if (currentVendor != null)
			throw new ServiceException(200001, "A vendor ("
					+ VendorServiceProvider.provide().getCurrentVendor().code
					+ ") already exists.");

		UserServiceProvider.provide().addUser(input.admin);

		input.vendor.address = AddressServiceProvider.provide()
				.addAddress(input.vendor.address);
		input.vendor.logo = ResourceServiceProvider.provide()
				.addResource(input.vendor.logo);
		input.vendor.users(Arrays.asList(input.admin));
		input.vendor = VendorServiceProvider.provide().addVendor(input.vendor);

		IBankAccountService bankAccountService = BankAccountServiceProvider
				.provide();
		for (BankAccount currentAccount : input.vendor.accounts) {
			if (currentAccount.vendor == null) {
				currentAccount.vendor = input.vendor;
			}
			currentAccount.id = bankAccountService
					.addBankAccount(currentAccount).id;
		}

		CurrencyServiceProvider.provide().setupCurrencies();
	}

	@Override
	protected SetupVendorResponse newOutput () {
		return new SetupVendorResponse();
	}

	@Override
	protected Logger logger () {
		return LOG;
	}

}
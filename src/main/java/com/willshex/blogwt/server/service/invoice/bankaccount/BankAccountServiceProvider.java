//  
//  BankAccountServiceProvider.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 25, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.bankaccount;

import com.willshex.service.ServiceDiscovery;

final public class BankAccountServiceProvider {

	/**
	 * @return
	 */
	public static IBankAccountService provide () {
		IBankAccountService bankAccountService = null;

		if ((bankAccountService = (IBankAccountService) ServiceDiscovery
				.getService(IBankAccountService.NAME)) == null) {
			bankAccountService = BankAccountServiceFactory
					.createNewBankAccountService();
			ServiceDiscovery.registerService(bankAccountService);
		}

		return bankAccountService;
	}

}
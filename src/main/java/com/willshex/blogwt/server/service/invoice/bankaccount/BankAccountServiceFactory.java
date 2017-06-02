//  
//  BankAccountServiceFactory.java
//  xsdwsdl2code
//
//  Created by William Shakour on April 25, 2015.
//  Copyright © 2015 SPACEHOPPER STUDIOS. All rights reserved.
//
package com.willshex.blogwt.server.service.invoice.bankaccount;

final class BankAccountServiceFactory {

	/**
	 * @return
	 */
	public static IBankAccountService createNewBankAccountService () {
		return new BankAccountService();
	}

}